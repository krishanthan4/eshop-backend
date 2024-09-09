package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
import entity.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet("/Verification")
public class Verification extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      Gson gson = new Gson();
Response_DTO responsedto = new Response_DTO();

Session session = null;

try {
    session = HibernateUtil.getSessionFactory().openSession();
    JsonObject requestJson = gson.fromJson(request.getReader(), JsonObject.class);
    String verification = requestJson.has("verification") ? requestJson.get("verification").getAsString() : null;

    // Ensure email exists in session before accessing it
    String email = (String) request.getSession().getAttribute("email");  // Ensure consistent attribute key

    if (email != null && verification != null && !verification.isEmpty()) {
    System.out.println("email is : " + email);

        System.out.println("verification : "+verification);
        session.beginTransaction();  // Start transaction before querying the database

        // Create criteria to find the user by email and verification code
        Criteria criteria1 = session.createCriteria(User.class);
        criteria1.add(Restrictions.eq("email", email));
        criteria1.add(Restrictions.eq("verificationCode", verification));

        // Fetch the result once
        User user = (User) criteria1.uniqueResult();  // uniqueResult is better than list() if you expect one record
System.out.println("User found: " + (user != null));

        if (user != null) {
            user.setVerificationCode("verified");
            session.update(user);
            session.getTransaction().commit();  // Commit the transaction after the update

            // Prepare response
            User_DTO userdto = new User_DTO();
            userdto.setEmail(email);

            request.getSession().removeAttribute("email");  // Clear session attribute
            request.getSession().setAttribute("user", userdto);  // Set the updated user in session

            responsedto.setSuccess(true);
            responsedto.setContent("User Verified Successfully!");
        } else {
            responsedto.setContent("Verification Code or Email Not Valid!");
        }

    } else if (email == null) {
        responsedto.setContent("Verification Code Unavailable. Please Sign In.");
    } else {
        responsedto.setContent("Verification Code Cannot Be Empty");
    }

} catch (Exception e) {
    if (session != null && session.getTransaction() != null) {
        session.getTransaction().rollback();  // Rollback transaction in case of failure
    }
    responsedto.setContent("Something Went Wrong");
    System.out.println("Error : " + e);
} finally {
    if (session != null) {
        session.close();  // Close the session to prevent memory leaks
    }
}

// Set response type to JSON and send the response
response.setContentType("application/json");
response.getWriter().write(gson.toJson(responsedto));

    }

}
