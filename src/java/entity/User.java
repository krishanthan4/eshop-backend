package entity;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "email", nullable = false, length = 100,unique=true)
    private String email;

    @Column(name = "fname", length = 50)
    private String fname;

    @Column(name = "lname", length = 45)
    private String lname;

    @Column(name = "password", nullable = false, length = 20)
    private String password;

    @Column(name = "mobile", length = 10)
    private String mobile;

    @Column(name = "joined_date", nullable = false)
    private LocalDateTime joinedDate;

    @Column(name = "verification_code", length = 20)
    private String verificationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_gender_id")
    private int genderGender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_status_id", nullable = false)
    private int statusStatus;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public LocalDateTime getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(LocalDateTime joinedDate) {
        this.joinedDate = joinedDate;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public int getGenderGender() {
        return genderGender;
    }

    public void setGenderGender(int genderGender) {
        this.genderGender = genderGender;
    }

    public int getStatusStatus() {
        return statusStatus;
    }

    public void setStatusStatus(int statusStatus) {
        this.statusStatus = statusStatus;
    }

}