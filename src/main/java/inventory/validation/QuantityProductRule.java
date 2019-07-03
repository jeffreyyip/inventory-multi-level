package inventory.validation;

import inventory.Product;
import inventory.validation.ProductRuleBase;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class QuantityProductRule extends ProductRuleBase {

    @Override
    public Map<String, String> invalidate(Product p) {

        if ( p.getQuantity() < 0 ){
            error.put("quantity", "Product.quantity");
            return error;
        }

        if (next != null){
            return next.invalidate(p);
        }

        return null;



    }

}
