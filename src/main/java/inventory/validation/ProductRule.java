package inventory.validation;

import inventory.Product;

import java.util.Map;

/**
 * validation rule for Product object
 */
public interface ProductRule {
    Map<String, String> invalidate(Product product);

    void setNext(ProductRule next);

}
