package entity;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class InvoiceHasProductId implements java.io.Serializable {
    private static final long serialVersionUID = -3382159653278525820L;
    @Column(name = "invoice_id", nullable = false)
    private Integer invoiceId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InvoiceHasProductId entity = (InvoiceHasProductId) o;
        return Objects.equals(this.productId, entity.productId) &&
                Objects.equals(this.invoiceId, entity.invoiceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, invoiceId);
    }

}