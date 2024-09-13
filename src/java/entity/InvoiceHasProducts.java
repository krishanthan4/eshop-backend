package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "invoiceHasProducts")
public class InvoiceHasProducts implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "invoiceId")
    private Invoice invoice;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
    
    @Column(name = "boughtQty")
    private int boughtQty;

    @Column(name = "orderStatus")
    private int orderStatus;

    public InvoiceHasProducts() {}

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getBoughtQty() {
        return boughtQty;
    }

    public void setBoughtQty(int boughtQty) {
        this.boughtQty = boughtQty;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    
}