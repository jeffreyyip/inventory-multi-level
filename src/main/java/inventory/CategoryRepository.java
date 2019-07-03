package inventory;

import inventory.Category;
import inventory.CategoryType;
import inventory.CategoryQueryRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository  extends CrudRepository<Category, Long>, CategoryQueryRepository {

    List<Category> findByCategoryType(@Param("categoryType") CategoryType categoryType);

    List<Category> findByParentCategory(@Param("categoryType") CategoryType categoryType);

    // used by GUI to get the top categories
    List<Category> findByTopCategory();
}
