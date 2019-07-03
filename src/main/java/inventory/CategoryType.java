package inventory;

/**
 * It is the chain of Category;
 * to represent the categories of the product.
 * It supports MULTI-LEVEL categories;
 * by linking category to its parent category
 * ;one category has only one or no parent category.
 * If there is no parent category, the category is the TOP category in the chain.
 */
public enum CategoryType {

    CLOTHES(null),
    FOOD(null),
    HOUSEHOLD(null),

    SHOES(CLOTHES), SOCKS(CLOTHES), JACKET(CLOTHES),
    FRUIT(FOOD), CANDY(FOOD), CAKE(FOOD),
    SOFA(HOUSEHOLD), CHAIR(HOUSEHOLD);




    private CategoryType parent;


    CategoryType(CategoryType parent){
        this.parent = parent;
    }

    public CategoryType getParent() {
        return this.parent;
    }


}
