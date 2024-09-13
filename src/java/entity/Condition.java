package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "`condition`")
public class Condition implements Serializable{
    @Id
    @Column(name = "conditionId")
    private int id;

    @Column(name = "conditionName")
    private String conditionName;

    public Condition() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return conditionName;
    }

    public void setName(String conditionName) {
        this.conditionName = conditionName;
    }

}