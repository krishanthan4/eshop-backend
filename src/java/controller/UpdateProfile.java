package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.User_DTO;
import entity.Address;
import entity.Cart;
import entity.City;
import entity.Gender;
import entity.InvoiceHasProducts;
import entity.OrderStatus;
import entity.Invoice;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import model.Payhere;
import org.hibernate.HibernateException;
import util.config;

@WebServlet("/UpdateProfile")
public class UpdateProfile extends HttpServlet {

  @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Gson gson = new Gson();
    HttpSession httpsession = request.getSession();
    Session session = HibernateUtil.getSessionFactory().openSession();
    Transaction transaction = session.beginTransaction();
    JsonObject responseJsonObject = new JsonObject();
    responseJsonObject.addProperty("success", false);

    try {
        // Parse the incoming JSON request
        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        // Check if the user is logged in
        if (httpsession.getAttribute("user") != null) {
            String firstName = requestJsonObject.get("firstName").getAsString();
            String lastName = requestJsonObject.get("lastName").getAsString();
            String mobile = requestJsonObject.get("mobile").getAsString();
            String gender = requestJsonObject.get("gender").getAsString();
            String line1 = requestJsonObject.get("line1").getAsString();
            String line2 = requestJsonObject.get("line2").getAsString();
            String city = requestJsonObject.get("city").getAsString();
            String postalCode = requestJsonObject.get("postalCode").getAsString();

            // Validation checks
            if (firstName.isEmpty()) {
                responseJsonObject.addProperty("content", "Add Your First Name");
            } else if (lastName.isEmpty()) {
                responseJsonObject.addProperty("content", "Add Your Last Name");
            } else if (mobile.isEmpty()) {
                responseJsonObject.addProperty("content", "Add Your Mobile Number");
            } else if (gender.isEmpty()) {
                responseJsonObject.addProperty("content", "Select Your Gender");
            } else if (line1.isEmpty()) {
                responseJsonObject.addProperty("content", "Add Your Address Line 1");
            } else if (line2.isEmpty()) {
                responseJsonObject.addProperty("content", "Add Your Address Line 2");
            } else if (city.isEmpty()) {
                responseJsonObject.addProperty("content", "Select Your City");
            } else if (postalCode.isEmpty()) {
                responseJsonObject.addProperty("content", "Add Your Postal Code");
            } else if (!Validations.isTextValid(firstName)) {
                responseJsonObject.addProperty("content", "Add a valid First Name");
            } else if (!Validations.isTextValid(lastName)) {
                responseJsonObject.addProperty("content", "Add a valid Last Name");
            } else if (!Validations.isMobileNumberValid(mobile)) {
                responseJsonObject.addProperty("content", "Add a valid Mobile Number");
            } else if (!Validations.isInteger(city)) {
                responseJsonObject.addProperty("content", "Select a Valid City");
            } else if (!Validations.isPostalCodeValid(postalCode)) {
                responseJsonObject.addProperty("content", "Add a valid Postal Code");
            } else {
                // Get the user from the session and database
                String userEmail = httpsession.getAttribute("user").toString();
                Criteria userCriteria = session.createCriteria(User.class);
                userCriteria.add(Restrictions.eq("email", userEmail));
                User user = (User) userCriteria.uniqueResult();

                if (user == null) {
                    responseJsonObject.addProperty("content", "User not found.");
                    response.setContentType("application/json");
                    response.getWriter().write(gson.toJson(responseJsonObject));
                    return;
                }

                // Update user details
                user.setFname(firstName);
                user.setLname(lastName);
                user.setMobile(mobile);

                // Fetch and set gender
                Criteria genderCriteria = session.createCriteria(Gender.class);
                genderCriteria.add(Restrictions.eq("id", Integer.parseInt(gender)));
                Gender genderObj = (Gender) genderCriteria.uniqueResult();
                user.setGenderGender(genderObj);

                session.update(user);

                // Commit the user update transaction
                transaction.commit();
                transaction = session.beginTransaction();  // Start a new transaction for address update

                // Fetch city for address
                Criteria cityCriteria = session.createCriteria(City.class);
                cityCriteria.add(Restrictions.eq("cityId", Integer.parseInt(city)));
                City cityObj = (City) cityCriteria.uniqueResult();

                // Fetch or create address
                Criteria addressCriteria = session.createCriteria(Address.class);
                addressCriteria.add(Restrictions.eq("user", user));
                Address address = (Address) addressCriteria.uniqueResult();

                if (address == null) {
                    // Create new address if not found
                    address = new Address();
                    address.setUser(user);
                    responseJsonObject.addProperty("content", "User Address Added");
                } else {
                    responseJsonObject.addProperty("content", "User Address Updated");
                }

                // Update address details
                address.setCity(cityObj);
                address.setLine1(line1);
                address.setLine2(line2);
                address.setPostalCode(postalCode);

                session.saveOrUpdate(address);
                transaction.commit();  // Commit the address update transaction
                responseJsonObject.addProperty("success", true);
            }
        } else {
            // User not signed in
            responseJsonObject.addProperty("content", "User not signed in.");
        }

        // Send the response as JSON
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));

    } catch (HibernateException e) {
        // Rollback the transaction in case of an exception
        if (transaction != null) {
            transaction.rollback();
        }
        System.out.println("Error: " + e);
        responseJsonObject.addProperty("content", "Error occurred during processing.");
    } finally {
        // Ensure session is closed
        if (session != null && session.isOpen()) {
            session.close();
        }
    }
}


}
