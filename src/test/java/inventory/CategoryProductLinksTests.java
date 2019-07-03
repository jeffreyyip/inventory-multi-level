package inventory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class CategoryProductLinksTests {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    private static String CATEGORY_ENDPOINT ="http://localhost:8080/categories";
    private static String PRODUCT_ENDPOINT = "http://localhost:8080/product";

    private static String SEARCH_TOP_CATEGORY = "/search/findByTopCategory";
    private static String SEARCH_BY_CATEGORY = "/search/findByCategoryType";
    private static String SEARCH_CATEGORY_KEY = "categoryType";
    private static String CategoryName = "CLOTHES";
    private static String SubCategoryName = "SHOES";
    private static String productName = "myShoes1";
    private static int quantity = 100;


    @Before
    public void deleteAllandInsertCategoryWithParentBeforeTests() throws Exception {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        for (CategoryType c : CategoryType.values()) {
            categoryRepository.save(new Category(c));
        }



        for (CategoryType c: CategoryType.values()){
            List<Category> entities = categoryRepository.findByCategoryType(c);

            for (Category entity : entities){
                List<Category> parents = categoryRepository.findByCategoryType(c.getParent());
                if (parents.size() > 0) {
                    entity.setParentCategory(parents.get(0));

                    categoryRepository.save(entity);

                }
            }
        }

    }

    @Test
    public void getTopCategories() throws Exception{

        // get the top categories and asserting them.

        mockMvc.perform(
                get(CATEGORY_ENDPOINT+ SEARCH_TOP_CATEGORY)).andExpect(
                status().isOk())
                .andExpect(jsonPath("$._embedded.categories", hasSize(3)))
                .andExpect(jsonPath("$._embedded.categories[0].categoryType", anyOf(is(CategoryType.CLOTHES.name()), is(CategoryType.FOOD.name()), is(CategoryType.HOUSEHOLD.name()) )) )
                .andExpect(jsonPath("$._embedded.categories[1].categoryType", anyOf(is(CategoryType.CLOTHES.name()), is(CategoryType.FOOD.name()), is(CategoryType.HOUSEHOLD.name()) )))
                .andExpect(jsonPath("$._embedded.categories[2].categoryType", anyOf(is(CategoryType.CLOTHES.name()), is(CategoryType.FOOD.name()), is(CategoryType.HOUSEHOLD.name()) )));

    }


    @Test
    public void shouldReturnChildCategoriesForParentCategory() throws Exception{

        ObjectMapper mapper = new ObjectMapper();
        String response;

        // Parent CategoryType
        CategoryType parentCategoryType = CategoryType.FOOD;

        // get Detail links of Parent CategoryType
        response = template.getForObject(CATEGORY_ENDPOINT+SEARCH_BY_CATEGORY + "?" +SEARCH_CATEGORY_KEY + "="  + parentCategoryType.name(), String.class);

        // parse the url for getting subCategories
        String subCategoryUrl = mapper.readTree(response).path("_embedded").path("categories").get(0).findValue("subCategories").findValue("href").asText();

        // get Detail of subCategories
        response = template.getForObject( subCategoryUrl, String.class);

        // parse the name of subCategories
        List<String> subCategories = mapper.readTree(response).path("_embedded").path("categories").findValuesAsText("categoryType");

        // asserting subCategories are child Categories
        subCategories.forEach( c -> Assert.assertTrue(CategoryType.valueOf(c).getParent() == parentCategoryType) );

    }

    @Test
    public void shouldReturnProductInLowestCategoryLevel() throws Exception{

        ObjectMapper mapper = new ObjectMapper();
        String response;

        // Parent CategoryType
        CategoryType parentCategoryType = CategoryType.CLOTHES;
        // Sub-CategoryType
        CategoryType subCategoryType = CategoryType.SHOES;
        String productName = "Shoes1";
        int quantity = 100;

        Product product = new Product(productName, quantity, parentCategoryType, subCategoryType);
        // create product
        mockMvc.perform(post("/product").content(
                toJson(product))).andExpect(
                status().isCreated()).andExpect(
                header().string("Location", containsString("product/")));


        // get Detail of Parent CategoryType
        response = template.getForObject(CATEGORY_ENDPOINT+SEARCH_BY_CATEGORY + "?" +SEARCH_CATEGORY_KEY + "="  + parentCategoryType.name(), String.class);

        // parse the url for getting subCategories
        String subCategoryUrl = mapper.readTree(response).path("_embedded").path("categories").get(0).findValue("subCategories").findValue("href").asText();

        // get Detail of subCategories
        response = template.getForObject( subCategoryUrl, String.class);

        // parse the products link of the subCategory that we are looking at
        String productsLink="";
        for (Iterator<JsonNode> it = mapper.readTree(response).path("_embedded").path("categories").iterator(); it.hasNext(); ){
            JsonNode node = it.next();
            if (node.get("categoryType").asText().equals(subCategoryType.name())) {

                productsLink = node.get("_links").findValue("products").findValue("href").asText();
            }
        }

        //get the detail of the products link and assert that the product we created is there
        response = template.getForObject(productsLink, String.class);

        // parse the name of products
        List<String> productNames = mapper.readTree(response).path("_embedded").path("product").findValuesAsText("name");

        // asserting that product name is the one we created.
        Assert.assertTrue(productNames.contains(productName));



    }

    private String toJson(Product p) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(p);

    }



}
