package entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "district")
public class District implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "districtId", nullable = false)
    private Integer districtId;

    @Column(name = "districtName")
    private String districtName;

    @ManyToOne
    @JoinColumn(name = "provinceProvinceId")
    private Province province;

    public District() {
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }


    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

}
