package entity;

import javax.persistence.*;

@Entity
@Table(name = "category_has_brand")
public class CategoryHasBrand {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_cat_id", nullable = false)
    private Category categoryCat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_brand_id", nullable = false)
    private Brand brandBrand;

    public Category getCategoryCat() {
        return categoryCat;
    }

    public void setCategoryCat(Category categoryCat) {
        this.categoryCat = categoryCat;
    }

    public Brand getBrandBrand() {
        return brandBrand;
    }

    public void setBrandBrand(Brand brandBrand) {
        this.brandBrand = brandBrand;
    }

}