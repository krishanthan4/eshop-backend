package entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "province")
public class Province implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provinceId", nullable = false)
    private Integer provinceId;
    
    @Column(name="provinceName")
    private String provinceName;

    public Province() {
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
    
   
      
      
}
