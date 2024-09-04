package entity;

import javax.persistence.*;

@Entity
@Table(name = "invoice_has_products")
public class InvoiceHasProduct {
    @EmbeddedId
    private InvoiceHasProductId id;

    @MapsId("invoiceId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "bought_qty", nullable = false)
    private Integer boughtQty;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    public InvoiceHasProductId getId() {
        return id;
    }

    public void setId(InvoiceHasProductId id) {
        this.id = id;
    }

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

    public Integer getBoughtQty() {
        return boughtQty;
    }

    public void setBoughtQty(Integer boughtQty) {
        this.boughtQty = boughtQty;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

}