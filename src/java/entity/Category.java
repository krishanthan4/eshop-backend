package entity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "category")
public class Category implements Serializable{
    @Id
    @Column(name = "catId", nullable = false)
    private Integer id;

    @Column(name = "catName", length = 50)
    private String catName;

    @Lob
    @Column(name = "catImg")
    private String catImg;

    @Lob
    @Column(name = "catIcon", nullable = false)
    private String catIcon;

    public Category() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImg() {
        return catImg;
    }

    public void setCatImg(String catImg) {
        this.catImg = catImg;
    }

    public String getCatIcon() {
        return catIcon;
    }

    public void setCatIcon(String catIcon) {
        this.catIcon = catIcon;
    }

}