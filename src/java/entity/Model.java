package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "model")
public class Model implements Serializable{
    @Id
    @Column(name = "modelId", nullable = false)
    private Integer id;

    @Column(name = "modelName", nullable = false)
    private String modelName;

        @ManyToOne
    @JoinColumn(name = "categoryCatId")
    private Category categoryCatId;
        
    public Model() {
    }


}