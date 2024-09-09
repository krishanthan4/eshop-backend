package entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "modelHasBrand")
public class ModelHasBrand implements Serializable{
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "modelModelId", nullable = false)
    private Model modelModel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "brandBrandId", nullable = false)
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