package entity;

import java.io.Serializable;
import javax.persistence.*;


@Entity
@Table(name="productImg")
public class ProductImg implements Serializable{
    @Id
    @Column(name="imgPath")
    private String imgPath;
    
    @ManyToOne
    @JoinColumn(name="productId")
    private Product product;

    public ProductImg() {
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    
}
