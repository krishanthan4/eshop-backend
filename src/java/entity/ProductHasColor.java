package entity;

import javax.persistence.*;

@Entity
@Table(name = "product_has_color")
public class ProductHasColor {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "color_clr_id", nullable = false)
    private Color colorClr;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Color getColorClr() {
        return colorClr;
    }

    public void setColorClr(Color colorClr) {
        this.colorClr = colorClr;
    }

}