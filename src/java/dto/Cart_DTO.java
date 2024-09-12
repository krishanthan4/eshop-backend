package dto;

import entity.Product;
import java.io.Serializable;


public class Cart_DTO implements Serializable{
    private Product product;
    private int qty;
private int cartId;
    public Cart_DTO() {
    }

    
    public int getCartId() {
        return cartId;
    }

    /**
     * @return the product
     */
    public void setCartId(int cartId) {    
        this.cartId = cartId;
    }

    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return the qty
     */
    public int getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(int qty) {
        this.qty = qty;
    }
    
}
