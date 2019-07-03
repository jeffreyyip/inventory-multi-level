package inventory;

import javax.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private int quantity;

    // these transient fields are getting from GUI
    // or populated by post-load event
    transient private CategoryType categoryType;
    transient private CategoryType subCategoryType;

    // only the lowest level of Category chain is stored.
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @PostLoad
    void fillSubCategory(){

        if (category !=null){
            this.subCategoryType = category.getCategoryType();
            this.categoryType = subCategoryType.getParent();
        }
    }
    public Product(){ }

    public Product(String name, int quantity, CategoryType parentType, CategoryType subType){
        this.name = name;
        this.quantity = quantity;
        this.categoryType = parentType;
        this.subCategoryType = subType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public CategoryType getSubCategoryType() {
        return subCategoryType;
    }

    public void setSubCategoryType(CategoryType subCategoryType) {
        this.subCategoryType = subCategoryType;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
