package entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "color")
public class Color implements Serializable {

    @Id
    @Column(name = "clrId", nullable = false)
   private int clrId;

    @Column(name = "clrName", nullable = false,length = 20)
   private String clrName;

    public Color() {
    }

    public int getClrId() {
        return clrId;
    }

    public void setClrId(int clrId) {
        this.clrId = clrId;
    }

    public String getClrName() {
        return clrName;
    }

    public void setClrName(String clrName) {
        this.clrName = clrName;
    }
    
}
