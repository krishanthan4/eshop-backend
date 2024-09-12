package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "price", nullable = true)
    private Double price;

    @Column(name = "qty", nullable = true)
    private Integer qty;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "title", length = 100, nullable = true)
    private String title;

    @Column(name = "datetimeAdded", nullable = true)
    private Date datetimeAdded;

    @Column(name = "deliveryFee", nullable = true)
    private Double deliveryFee;

    @ManyToOne
    @JoinColumn(name = "modelModelId")
    private Model model;

    @ManyToOne
    @JoinColumn(name = "colorClrId")
    private Color color;

    @ManyToOne
    @JoinColumn(name = "conditionConditionId")
    private Condition condition;

    @ManyToOne
    @JoinColumn(name = "statusStatusId")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "userEmail")
    private User userEmail;

    // Default constructor
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

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(User userEmail) {
        this.userEmail = userEmail;
    }

}
