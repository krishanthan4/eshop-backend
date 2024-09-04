package entity;

import javax.persistence.*;

@Entity
@Table(name = "city")
public class City {
    @Id
    @Column(name = "city_id", nullable = false)
    private Integer id;

    @Column(name = "city_name", length = 45)
    private String cityName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "district_district_id", nullable = false)
    private District districtDistrict;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public District getDistrictDistrict() {
        return districtDistrict;
    }

    public void setDistrictDistrict(District districtDistrict) {
        this.districtDistrict = districtDistrict;
    }

}