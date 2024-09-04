package entity;

import javax.persistence.*;

@Entity
@Table(name = "model_has_brand")
public class ModelHasBrand {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "model_model_id", nullable = false)
    private Model modelModel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brand_brand_id", nullable = false)
    private Brand brandBrand;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Model getModelModel() {
        return modelModel;
    }

    public void setModelModel(Model modelModel) {
        this.modelModel = modelModel;
    }

    public Brand getBrandBrand() {
        return brandBrand;
    }

    public void setBrandBrand(Brand brandBrand) {
        this.brandBrand = brandBrand;
    }

}