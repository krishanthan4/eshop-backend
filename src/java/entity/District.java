package entity;

import javax.persistence.*;

@Entity
@Table(name = "district")
public class District {
    @Id
    @Column(name = "district_id", nullable = false)
    private Integer id;

    @Column(name = "district_name", length = 45)
    private String districtName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "province_province_id", nullable = false)
    private Province provinceProvince;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Province getProvinceProvince() {
        return provinceProvince;
    }

    public void setProvinceProvince(Province provinceProvince) {
        this.provinceProvince = provinceProvince;
    }

}