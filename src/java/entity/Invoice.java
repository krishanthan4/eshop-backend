package entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "invoice")
public class Invoice {
    @Id
    @Column(name = "invoice_id", nullable = false)
    private Integer id;

    @Column(name = "order_id", length = 20)
    private String orderId;

    @Column(name = "date")
    private Instant date;

    @Column(name = "total")
    private Double total;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_email", nullable = false)
    private User userEmail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public User getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(User userEmail) {
        this.userEmail = userEmail;
    }

}