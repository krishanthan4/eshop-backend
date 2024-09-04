package entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "price")
    private Double price;

    @Column(name = "qty")
    private Integer qty;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "datetime_added")
    private Instant datetimeAdded;

    @Column(name = "delivery_fee_colombo")
    private Double deliveryFeeColombo;

    @Column(name = "delivery_fee_other")
    private Double deliveryFeeOther;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_cat_id", nullable = false)
    private Category categoryCat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "model_has_brand_id", nullable = false)
    private ModelHasBrand modelHasBrand;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "condition_condition_id", nullable = false)
    private Condition conditionCondition;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_status_id", nullable = false)
    private Status statusStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_email", nullable = false)
    private User userEmail;

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

    public Instant getDatetimeAdded() {
        return datetimeAdded;
    }

    public void setDatetimeAdded(Instant datetimeAdded) {
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