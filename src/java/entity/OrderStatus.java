package entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="orderStatus")
public class OrderStatus implements Serializable{
    
    @Id
    @Column(name="orderStatusId")
    private int orderStatusId;
    
    @Column(name="orderStatusName")
    private String orderStatusName;

    public OrderStatus() {
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }
}
