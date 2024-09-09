package entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "categoryHasBrand")
public class CategoryHasBrand implements Serializable{
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoryCatId", nullable = false)
    private Category categoryCat;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brandBrandId", nullable = false)
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