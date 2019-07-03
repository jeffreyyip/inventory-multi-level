FEATURES:
Multi-level of Category hierarchy in backend server ( not only 2 levels  ); so the system could be expanded easily.
REST link shows top categories.
REST link shows the parent-child relationship on each category level.
REST link shows the category to parent relationship
Validation rule for business domain (e.g. Product) could be plug-in as easy as annotation.
Saving database space by fine-grained Entity relationship.
Category could be config to persist automatically for testing environment.

How to BUILD:
mvn clean package

How to RUN:
java -jar target/inventory-multi-level-0.0.1-SNAPSHOT.jar


Here are entry URLs:

For REST API with Hypermedia links (HATEOAS)
http://localhost:8080/
http://localhost:8080/product
http://localhost:8080/categories
http://localhost:8080/categories/search/findByTopCategory



For Web GUI:
http://localhost:8080/web
(it will redirect to inventory.html)
The GUI support listing of product, and to create, update and delete product.

For in-memory datbase:
http://localhost:8080/h2-console
(url: jdbc:h2:mem:testdb )
(user "sa" without password)

ASSUMPTION:
Category and SubCategory would not changed much;
Categories are saved into database as master source
and use Enum String instead Ordinal in database;
using entity relationship to represent each category level;

The possible category values and its parent-child relationship are retrieved by the client using the Hypermedia links
(http://localhost:8080/categories)

Top categories could be retrieved by link:
http://localhost:8080/categories/search/findByTopCategory


Database is using as in-memory H2 database; other dataStore could be used and be config.


LIMITATION:
The Product is linked to the lowest level of Category only in database;
so it might be slow to retrieve product list by top category through the parent-child relationship.


The GUI to Server interface (Product) is hard-coded to support 2 level of Category for now;
but it could be easily changed to support multiple level using array of categories in JSON.



