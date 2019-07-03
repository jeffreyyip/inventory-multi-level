package inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * when POST and PUT product, setting up the lowest level category to the product.
 */
@Component
@RepositoryEventHandler
public class ProductEventHandler {

    @Autowired
    CategoryRepository categoryRepository;

    @HandleBeforeCreate
    public void handleProductCreate(Product p) {


        if (p.getSubCategoryType() != null ){
            List<Category> category = categoryRepository.findByCategoryType(p.getSubCategoryType());

            if (! category.isEmpty()){
                p.setCategory(category.get(0));

            }

        }


    }

    @HandleBeforeSave
    public void handleProductSave(Product p) {

        if (p.getSubCategoryType() != null ){
            List<Category> category = categoryRepository.findByCategoryType(p.getSubCategoryType());

            if (! category.isEmpty()){
                p.setCategory(category.get(0));

            }

        }

    }
}
