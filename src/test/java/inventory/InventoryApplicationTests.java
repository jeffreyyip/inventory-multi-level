package inventory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InventoryApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class InventoryApplicationTests {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Before
    public void deleteAllBeforeTests() throws Exception {
        productRepository.deleteAll();

            categoryRepository.deleteAll();

            // setup Category
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
    public void shouldCreateEntity() throws Exception {

        Product product = new Product("AirJordan", 100, CategoryType.CLOTHES, CategoryType.SHOES);

        mockMvc.perform(post("/product").content(
                toJson(product))).andExpect(
                status().isCreated()).andExpect(
                header().string("Location", containsString("product/")));


    }

    @Test
    public void shouldRetrieveEntity() throws Exception {

        Product product = new Product("AirJordan", 100, CategoryType.CLOTHES, CategoryType.SHOES);


        MvcResult mvcResult = mockMvc.perform(post("/product").content(
                toJson(product))).andExpect(
                status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                jsonPath("$.name").value(product.getName())).andExpect(
                jsonPath("$.quantity").value(product.getQuantity())).andExpect(
                jsonPath("$.categoryType").value(product.getCategoryType().name())).andExpect(
                jsonPath("$.subCategoryType").value(product.getSubCategoryType().name()));
    }


    @Test
    public void shouldQueryEntity() throws Exception {

        Product product = new Product("AirJordan", 100, CategoryType.CLOTHES, CategoryType.SHOES);

        mockMvc.perform(post("/product").content(
                toJson(product))).andExpect(
                status().isCreated());

        mockMvc.perform(
                get("/product/search/findByName?name={name}", product.getName())).andExpect(
                status().isOk()).andExpect(
                jsonPath("$._embedded.product[0].name").value(
                        product.getName()));
    }

    @Test
    public void shouldUpdateEntity() throws Exception {

        Product product = new Product("AirJordan", 100, CategoryType.CLOTHES, CategoryType.SHOES);
        Product updatedProduct = new Product("AirJordan"+"X", 100+100, CategoryType.CLOTHES, CategoryType.SHOES);

        MvcResult mvcResult = mockMvc.perform(post("/product").content(
                toJson(product))).andExpect(
                status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");

        mockMvc.perform(put(location).content(
                toJson(updatedProduct))).andExpect(
                status().isNoContent());

        mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                jsonPath("$.name").value(updatedProduct.getName())).andExpect(
                jsonPath("$.quantity").value(updatedProduct.getQuantity())).andExpect(
                jsonPath("$.categoryType").value(updatedProduct.getCategoryType().name())).andExpect(
                jsonPath("$.subCategoryType").value(updatedProduct.getSubCategoryType().name()));
    }


    @Test
    public void shouldDeleteEntity() throws Exception {

        Product product = new Product("AirJordan", 100, CategoryType.CLOTHES, CategoryType.SHOES);

        MvcResult mvcResult = mockMvc.perform(post("/product").content(
                toJson(product))).andExpect(
                status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc.perform(delete(location)).andExpect(status().isNoContent());

        mockMvc.perform(get(location)).andExpect(status().isNotFound());
    }

    @Test
    public void shouldNOTCreateEntity_whenMismatchCategories() throws Exception {

        Product mismatchCategories = new Product("AirJordan", 100, CategoryType.CLOTHES, CategoryType.SOFA);

        mockMvc.perform(post("/product").content(
                toJson(mismatchCategories))).andExpect(
                status().isBadRequest());

    }

    @Test
    public void shouldNOTUpdateEntity_whenMismatchCategories() throws Exception {

        Product product = new Product("AirJordan", 100, CategoryType.CLOTHES, CategoryType.SHOES);

        Product mismatchCategories = new Product("AirJordan", 100, CategoryType.CLOTHES, CategoryType.SOFA);

        MvcResult mvcResult = mockMvc.perform(post("/product").content(
                toJson(product))).andExpect(
                status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");

        mockMvc.perform(put(location).content(
                toJson(mismatchCategories))).andExpect(
                status().isBadRequest());


        mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                jsonPath("$.name").value(product.getName())).andExpect(
                jsonPath("$.quantity").value(product.getQuantity())).andExpect(
                jsonPath("$.categoryType").value(product.getCategoryType().name())).andExpect(
                jsonPath("$.subCategoryType").value(product.getSubCategoryType().name()));

    }


    @Test
    public void shouldNOTCreateEntity_whenWrongCategory() throws Exception {

        String wrongCategory = "Accessory";
        String json = toJson("AirJordan", 100, wrongCategory, "SHOES");
        mockMvc.perform(post("/product").content(
                json)).andExpect(
                status().isBadRequest());

    }

    @Test
    public void shouldNOTCreateEntity_whenWrongSubcategory() throws Exception {

        String wrongSubCategory = "hairCut";
        String json = toJson("AirJordan", 100, "CLOTHES", wrongSubCategory);
        mockMvc.perform(post("/product").content(
                json)).andExpect(
                status().isBadRequest());

    }

    private String toJson(Product p) throws Exception{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(p);


    }

    // this function is used when the category string is not part of Enum
    // and hence can not create Product object for Json mapper

    private String toJson(String name, int quantity, String category, String subCategory){
        return new StringBuilder().append("{\"name\":\"").append(name).append("\"")
                .append(",\"quantity\":").append(quantity)
                .append(",\"categoryType\":\"").append(category).append("\"")
                .append(",\"subCategoryType\":\"").append(subCategory).append("\"")
                .append("}").toString();
    }


}
