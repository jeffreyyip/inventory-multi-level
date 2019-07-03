package inventory;

import inventory.Category;

import java.util.List;

public interface CategoryQueryRepository {
    List<Category> findByTopCategory();
}
