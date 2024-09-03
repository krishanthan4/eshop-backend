package model;

import java.io.Serializable;

public class User implements Serializable{
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String mobile;
    private String joined_date;
    private String verification_code;
    private int gender_gender_id;
    private int status_status_id;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getJoined_date() {
        return joined_date;
    }

    public void setJoined_date(String joined_date) {
        this.joined_date = joined_date;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public int getGender_gender_id() {
        return gender_gender_id;
    }

    public void setGender_gender_id(int gender_gender_id) {
        this.gender_gender_id = gender_gender_id;
    }

    public int getStatus_status_id() {
        return status_status_id;
    }

    public void setStatus_status_id(int status_status_id) {
        this.status_status_id = status_status_id;
    }
}
