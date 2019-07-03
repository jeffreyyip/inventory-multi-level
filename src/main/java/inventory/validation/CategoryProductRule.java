package inventory.validation;

import inventory.Product;
import inventory.validation.ProductRule;
import inventory.validation.ProductRuleBase;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CategoryProductRule extends ProductRuleBase implements ProductRule {


    @Override
    public Map<String, String> invalidate(Product p) {

        if( p.getCategoryType() == null ||
                p.getSubCategoryType() == null ||
                p.getSubCategoryType().getParent() != p.getCategoryType() ){

            error.put("subCategoryType", "Product.subCategoryType");
            return error;

        }

        if (next != null){
            return next.invalidate(p);
        }

        return null;

    }
}
