package inventory;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository  extends CrudRepository<Category, Long>, CategoryQuery {

    List<Category> findByCategoryType(@Param("categoryType") CategoryType categoryType);

    List<Category> findByParentCategory(Category category);

    // used by GUI to get the top categories
    List<Category> findByTopCategory();
}
