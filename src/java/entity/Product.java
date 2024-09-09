package entity;

import java.io.Serializable;
import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "product")
public class Product implements Serializable{
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "price",nullable=true)
    private Double price;

    @Column(name = "qty",nullable=true)
    private Integer qty;

    @Lob
    @Column(name = "description",nullable=true)
    private String description;

    @Column(name = "title", length = 100,nullable=true)
    private String title;

    @Column(name = "datetimeAdded",nullable=true)
    private Date datetimeAdded;

    @Column(name = "deliveryFeeColombo",nullable=true)
    private Double deliveryFeeColombo;

    @Column(name = "deliveryFeeOther",nullable=true)
    private Double deliveryFeeOther;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoryCatId", nullable = false)
    private Category categoryCat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "modelHasBrandId", nullable = false)
    private ModelHasBrand modelHasBrand;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conditionConditionId", nullable = false)
    private Condition conditionCondition;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "statusStatusId", nullable = false)
    private Status statusStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userEmail", nullable = false)
    private User userEmail;

    public Product() {
    }

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDatetimeAdded() {
        return datetimeAdded;
    }

    public void setDatetimeAdded(Date datetimeAdded) {
        this.datetimeAdded = datetimeAdded;
    }

    public Double getDeliveryFeeColombo() {
        return deliveryFeeColombo;
    }

    public void setDeliveryFeeColombo(Double deliveryFeeColombo) {
        this.deliveryFeeColombo = deliveryFeeColombo;
    }

    public Double getDeliveryFeeOther() {
        return deliveryFeeOther;
    }

    public void setDeliveryFeeOther(Double deliveryFeeOther) {
        this.deliveryFeeOther = deliveryFeeOther;
    }

    public Category getCategoryCat() {
        return categoryCat;
    }

    public void setCategoryCat(Category categoryCat) {
        this.categoryCat = categoryCat;
    }

    public ModelHasBrand getModelHasBrand() {
        return modelHasBrand;
    }

    public void setModelHasBrand(ModelHasBrand modelHasBrand) {
        this.modelHasBrand = modelHasBrand;
    }

    public Condition getConditionCondition() {
        return conditionCondition;
    }

    public void setConditionCondition(Condition conditionCondition) {
        this.conditionCondition = conditionCondition;
    }

    public Status getStatusStatus() {
        return statusStatus;
    }

    public void setStatusStatus(Status statusStatus) {
        this.statusStatus = statusStatus;
    }

    public User getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(User userEmail) {
        this.userEmail = userEmail;
    }

}