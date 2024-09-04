package entity;

import javax.persistence.*;

@Entity
@Table(name = "product_img")
public class ProductImg {
    @Id
    @Column(name = "img_path", nullable = false, length = 100)
    private String imgPath;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

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