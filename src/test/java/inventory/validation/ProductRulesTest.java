package inventory.validation;

import inventory.CategoryType;
import inventory.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductRulesTest {

    @Autowired
    private ProductRules rules;

    @Test
    public void shouldValidateMatchSubCategory(){
        Product product = new Product("Nike11", 100, CategoryType.CLOTHES, CategoryType.SHOES);

        Map<String, String> error = rules.invalidate(product);

        Assert.assertTrue(error == null || error.size() == 0);

    }

    @Test
    public void shouldInvalidateMismatchClothes(){
        Product product = new Product("Nike11", 100, CategoryType.CLOTHES, CategoryType.CAKE);

        Map<String, String> error = rules.invalidate(product);

        Assert.assertTrue(error.size() > 0);
        Assert.assertTrue(error.keySet().contains("subCategoryType"));
        Assert.assertTrue(error.values().contains("Product.subCategoryType"));

    }


    @Test
    public void shouldInvalidateMismatchFood(){
        Product product = new Product("Nike11", 100, CategoryType.FOOD, CategoryType.SHOES);

        Map<String, String> error = rules.invalidate(product);

        Assert.assertTrue(error.size() > 0);
        Assert.assertTrue(error.keySet().contains("subCategoryType"));
        Assert.assertTrue(error.values().contains("Product.subCategoryType"));

    }

    @Test
    public void shouldInvalidateMismatchHousehold(){
        Product product = new Product("Nike11", 100, CategoryType.HOUSEHOLD, CategoryType.SHOES);

        Map<String, String> error = rules.invalidate(product);

        Assert.assertTrue(error.size() > 0);
        Assert.assertTrue(error.keySet().contains("subCategoryType"));
        Assert.assertTrue(error.values().contains("Product.subCategoryType"));


    }

    @Test
    public void shouldInvalidateNegativeQuantity(){
        Product product = new Product("Nike11", -100, CategoryType.HOUSEHOLD, CategoryType.CHAIR);

        Map<String, String> error = rules.invalidate(product);

        Assert.assertTrue(error.size() > 0);
        Assert.assertTrue(error.keySet().contains("quantity"));
        Assert.assertTrue(error.values().contains("Product.quantity"));

    }

}
