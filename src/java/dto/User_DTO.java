package dto;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

public class User_DTO implements Serializable{

    @Expose
    private String email;
    
    @Expose(serialize=false,deserialize=true)
    private String password;

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
}
