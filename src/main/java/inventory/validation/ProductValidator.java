package inventory.validation;

import inventory.Product;
import inventory.validation.ProductRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Map;

public class ProductValidator implements Validator {

    @Autowired
    private ProductRules rules;

    @Override
    public boolean supports(Class<?> aClass) {
        return Product.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Product product = (Product) o;

        Map<String, String> error = rules.invalidate(product);

        if (error != null && !error.isEmpty()){
            error.forEach( (k, v) -> errors.rejectValue(k, v));

        }

    }

}