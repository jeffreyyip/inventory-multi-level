package inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryQueryRepositoryImpl implements CategoryQueryRepository {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> findByTopCategory() {
        return categoryRepository.findByParentCategory(null);
    }
}
