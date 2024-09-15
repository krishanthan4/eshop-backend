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
        JsonObject requestObject = gson.fromJson(request.getReader(), JsonObject.class);
        int cartProductId = requestObject.get("cartProductId").getAsInt();
        System.out.println(cartProductId);
        // Open session for database interaction
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();  // Start a transaction

        String email2 = (String) httpSession.getAttribute("user");
//        ArrayList<Cart_DTO> cart_DTO_List = new ArrayList<>();

        // Fetch the user from the session
        Criteria userCriteria = session.createCriteria(User.class);
        userCriteria.add(Restrictions.eq("email", email2));
        User user = (User) userCriteria.uniqueResult();

        if (user != null) {
            Criteria productCriteria = session.createCriteria(Product.class);
            productCriteria.add(Restrictions.eq("id", cartProductId));
            Product product = (Product) productCriteria.uniqueResult();
            System.out.println("remove cart product"+gson.toJson(product));
            Criteria cartCriteria = session.createCriteria(Cart.class);
            cartCriteria.add(Restrictions.eq("user", user));  // Match user
            cartCriteria.add(Restrictions.eq("product", product));  // Match product

            // Fetch the Cart entity
            Cart cartItem = (Cart) cartCriteria.uniqueResult();
            System.out.println("remove cart cart"+gson.toJson(cartItem));

            // If the item exists, delete it
            if (cartItem != null) {
    session.delete(cartItem);
    session.flush();  // Ensure the delete is executed
    transaction.commit();  // Commit the transaction
    session.clear();  // Clear the session to avoid stale data
    jsonObject.addProperty("success", true);
    jsonObject.addProperty("content", "User cart item removed");

    System.out.println("Cart item removed successfully.");
} else {
    System.out.println("Cart item not found.");
}

            // Write the response
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(jsonObject));
            System.out.println("User Cart");
        }

    } catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();  // Rollback in case of error
        }
        jsonObject.addProperty("error", e.getMessage());
        System.out.println("Errors: " + e);

    } finally {
        if (session != null) {
            session.close();  // Ensure the session is closed
        }
    }
}

}
