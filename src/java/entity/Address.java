package entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addressId", nullable = false)
    private Integer addressId;
    
    @Column(name="line1")
    private String line1;
    
    @Column(name="line2")
    private String line2;
    
    @Column(name="postalCode")
    private String postalCode;
    
      @ManyToOne
    @JoinColumn(name = "cityCityId")
    private City city;
      
      @ManyToOne
      @JoinColumn(name="userEmail")
      private User user;

    public Address() {
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
      
      
}
