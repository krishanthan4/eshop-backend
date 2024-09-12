package model;

public class Validations {

    // Email validation using a more robust regex pattern
    public static boolean isEmailValid(String email) {
        return email.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    // Password validation to include at least one lowercase, one uppercase, one digit, one special character, and be at least 8 characters long
    public static boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    }
    
        public static boolean isDouble(String price) {

        return price.matches("^\\d+(\\.\\d{2})?$");

    }

    public static boolean isInteger(String value) {
        return value.matches("^\\d+$");
    }
    public static boolean  isMobileNumberValid(String mobile){
    return mobile.matches("^07[012345678]{1}[0-9]{7}$");
    }
}
