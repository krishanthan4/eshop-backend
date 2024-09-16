package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
import dto.Cart_DTO;
import entity.Cart;
import entity.Category;
import entity.Color;
import entity.Condition;
import entity.Gender;
import entity.Product;
import entity.Model;
import entity.ProductImg;
import entity.Status;
import entity.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet("/RemoveCart")
public class RemoveCart extends HttpServlet {

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    Gson gson = new Gson();
    JsonObject jsonObject = new JsonObject();
    Session session = null;
    HttpSession httpSession = request.getSession();
    Transaction transaction = null;  // Initialize a transaction variable

    try {
        // Parse request JSON
        JsonObject requestObject = gson.fromJson(request.getReader(), JsonObject.class);
        int cartProductId = requestObject.get("cartProductId").getAsInt();
        System.out.println("Cart Product ID: " + cartProductId);

        // Open session for database interaction
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();  // Start a transaction

        String email2 = (String) httpSession.getAttribute("user");

        // Fetch the user from the session
        Criteria userCriteria = session.createCriteria(User.class);
        userCriteria.add(Restrictions.eq("email", email2));
        User user = (User) userCriteria.uniqueResult();

        if (user != null) {
            // Fetch the product by its ID
            Criteria productCriteria = session.createCriteria(Product.class);
            productCriteria.add(Restrictions.eq("id", cartProductId));
            Product product = (Product) productCriteria.uniqueResult();
            System.out.println("Product to remove: " + gson.toJson(product));

            if (product != null) {
                // Fetch the Cart entity matching the user and product
                Criteria cartCriteria = session.createCriteria(Cart.class);
                cartCriteria.add(Restrictions.eq("user", user));  // Match user
                cartCriteria.add(Restrictions.eq("product", product));  // Match product
                Cart cartItem = (Cart) cartCriteria.uniqueResult();

                // If the cart item exists, delete it
                if (cartItem != null) {
                    session.delete(cartItem);       // Mark cartItem for deletion
                    session.flush();                // Ensure the DELETE statement is executed
                    transaction.commit();           // Commit the transaction to persist changes
                    session.clear();                // Clear the session after commit
                    HibernateUtil.getSessionFactory().getCache().evictEntity(Cart.class, cartItem);  // Evict from cache

                    jsonObject.addProperty("success", true);
                    jsonObject.addProperty("content", "User cart item removed");
                    System.out.println("Cart item removed successfully.");
                } else {
                    jsonObject.addProperty("success", false);
                    jsonObject.addProperty("message", "Cart item not found.");
                    System.out.println("Cart item not found.");
                }
            } else {
                jsonObject.addProperty("success", false);
                jsonObject.addProperty("message", "Product not found.");
                System.out.println("Product not found.");
            }
        } else {
            jsonObject.addProperty("success", false);
            jsonObject.addProperty("message", "User not found.");
            System.out.println("User not found.");
        }

        // Set response type and write the JSON output
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));

    } catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();  // Rollback transaction in case of an error
        }
        jsonObject.addProperty("error", e.getMessage());
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // Set error response code
        System.out.println("Errors: " + e);

        // Write error response
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));

    } finally {
        if (session != null) {
            session.close();  // Close the session in the finally block to avoid resource leaks
        }
    }
}

}
