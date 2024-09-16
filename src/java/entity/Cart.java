package entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Cacheable(false)
@Table(name="cart")
public class Cart implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cartId")
    private int id;
    
    @Column(name="qty")
    private int qty;
    
    @ManyToOne
    @JoinColumn(name="userEmail")        
    private User user;
    
    @ManyToOne
    @JoinColumn(name="productId")
    private Product product;

    public Cart() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    
    
}
