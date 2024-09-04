package entity;

import javax.persistence.*;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "cat_id", nullable = false)
    private Integer id;

    @Column(name = "cat_name", length = 50)
    private String catName;

    @Lob
    @Column(name = "cat_img")
    private String catImg;

    @Lob
    @Column(name = "cat_icon", nullable = false)
    private String catIcon;

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