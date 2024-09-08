package entity;

import java.io.Serializable;
import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "user")
public class User implements Serializable{
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
    private Date joinedDate;

    @Column(name = "verification_code", length = 20,nullable=true)
    private String verificationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gender_gender_id",nullable=true)
    private Gender genderGender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_status_id", nullable = false)
    private Status statusStatus;

    public User() {
    }

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

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Gender getGenderGender() {
        return genderGender;
    }

    public void setGenderGender(Gender genderGender) {
        this.genderGender = genderGender;
    }

    public Status getStatusStatus() {
        return statusStatus;
    }

    public void setStatusStatus(Status statusStatus) {
        this.statusStatus = statusStatus;
    }

}