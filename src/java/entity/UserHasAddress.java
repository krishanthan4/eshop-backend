package entity;

import javax.persistence.*;

@Entity
@Table(name = "user_has_address")
public class UserHasAddress {
    @Id
    @Column(name = "address_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_email", nullable = false)
    private User userEmail;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_city_id", nullable = false)
    private City cityCity;

    @Lob
    @Column(name = "line1")
    private String line1;

    @Lob
    @Column(name = "line2")
    private String line2;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(User userEmail) {
        this.userEmail = userEmail;
    }

    public City getCityCity() {
        return cityCity;
    }

    public void setCityCity(City cityCity) {
        this.cityCity = cityCity;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

}