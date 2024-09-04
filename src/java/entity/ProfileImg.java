package entity;

import javax.persistence.*;

@Entity
@Table(name = "profile_img")
public class ProfileImg {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "path", length = 100)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_email", nullable = false)
    private User userEmail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public User getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(User userEmail) {
        this.userEmail = userEmail;
    }

}