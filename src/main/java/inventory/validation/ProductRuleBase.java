package inventory.validation;

import inventory.validation.ProductRule;

import java.util.HashMap;
import java.util.Map;

public abstract class ProductRuleBase implements ProductRule {

    protected Map<String, String> error = new HashMap<>();
    protected ProductRule next;

    public void setNext(ProductRule next){
        this.next = next;
    }


}
