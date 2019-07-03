package inventory.validation;

import inventory.Product;
import inventory.validation.ProductRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * This component includes all others different ProductRule instances
 * and call them one by one;
 * if one failed, it return early and ignore other validation.
 */
@Component
class ProductRules {


    @Autowired
    List<ProductRule> ruleList;



    public ProductRules(List<ProductRule> rules){

        this.ruleList = rules;

        for (int i= 0; i < ruleList.size()-1; i++){
            ruleList.get(i).setNext(ruleList.get(i+1));
        }

    }

    Map<String, String> invalidate(Product p){

        return ruleList.get(0).invalidate(p);

    }
}
