package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "color")
public class Color {
    @Id
    @Column(name = "clr_id", nullable = false)
    private Integer id;

    @Column(name = "clr_name", length = 20)
    private String clrName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClrName() {
        return clrName;
    }

    public void setClrName(String clrName) {
        this.clrName = clrName;
    }

}