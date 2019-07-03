package inventory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryTypeRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Before
    public void deleteAllandInsertCategoryWithParentBeforeTests() throws Exception {
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
    public void shouldRetrieveAllCategoryWithParentCategory(){

        for (CategoryType c: CategoryType.values()){

            List<Category> entities = categoryRepository.findByCategoryType(c);

            Assert.assertEquals(entities.size(),1);
            Assert.assertEquals(entities.get(0).getCategoryType(), c);


            Category parent = entities.get(0).getParentCategory();

            if (parent != null){
                Assert.assertEquals( parent.getCategoryType() , c.getParent() );
            }
        }

    }

    @Test
    public void shouldRetrieveTopCategory(){
        List<Category> topCategories = categoryRepository.findByParentCategory(null);

        List<CategoryType> topTypes = topCategories.stream()
                                        .map( Category::getCategoryType )
                                        .collect(Collectors.toList());

        Assert.assertEquals(topCategories.size(),3);
        Assert.assertTrue(topTypes.contains(CategoryType.CLOTHES));
        Assert.assertTrue(topTypes.contains(CategoryType.FOOD));
        Assert.assertTrue(topTypes.contains(CategoryType.HOUSEHOLD));

    }

    @Test
    public void shouldRetrieveParentCategory(){
        List<Category> parent = categoryRepository.findByCategoryType(CategoryType.CLOTHES);
        List<Category> childCategories = categoryRepository.findByParentCategory(parent.get(0));

        for (Category child: childCategories) {
            Assert.assertTrue(child.getCategoryType().getParent()==CategoryType.CLOTHES);
        }

    }

}
