package inventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class InventoryApplication {

	// in testing environment, this variable could be set to persist category, for in-memory database
	// for production, set it to false/ not setting
	@Value("${persist.category:false}")
	private boolean isPersisCategory;

	private static final Logger log = LoggerFactory.getLogger(InventoryApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(InventoryApplication.class, args);
	}


	@Bean
	public CommandLineRunner setup(CategoryRepository categoryRepository){

		return (args) -> {
			if (isPersisCategory) {

				for (CategoryType c : CategoryType.values()) {
					categoryRepository.save(new Category(c));
					log.info("saving " + c);
				}


				for (CategoryType c : CategoryType.values()) {
					List<Category> entities = categoryRepository.findByCategoryType(c);

					for (Category entity : entities) {
						List<Category> parents = categoryRepository.findByCategoryType(c.getParent());
						if (parents.size() > 0) {
							entity.setParentCategory(parents.get(0));
							log.info("saving " + entity.toString());

							categoryRepository.save(entity);

						}
					}
				}

			}


		};
	}

}

