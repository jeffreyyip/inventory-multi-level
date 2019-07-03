package inventory;

import inventory.Category;

import java.util.List;

public interface CategoryQuery {
    List<Category> findByTopCategory();
}
