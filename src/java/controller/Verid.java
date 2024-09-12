package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dto.Cart_DTO;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.Category;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import entity.Model;
import entity.ProductImg;
import javax.servlet.http.HttpSession;
import org.hibernate.criterion.Order;

@WebServlet("/Verid")
public class Verid extends HttpServlet {
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    Gson gson = new Gson();
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("success", false);
    Session session = null;
                HttpSession httpSession = request.getSession();

    try {
        // Open session for database interaction
        session = HibernateUtil.getSessionFactory().openSession();
    User_DTO user_DTO = new User_DTO();
    user_DTO.setEmail((String) httpSession.getAttribute("user"));
        // Fetch the user from the session (replace "" with actual user email from the session or request)
        String userEmail = "email@fkdlk.com";
        Criteria userCriteria = session.createCriteria(User.class);
        userCriteria.add(Restrictions.eq("email", userEmail));
        User user = (User) userCriteria.uniqueResult();

        if (user != null) {
            // Fetch the cart items for the logged-in user
            Criteria cartCriteria = session.createCriteria(Cart.class);
            cartCriteria.add(Restrictions.eq("user", user));

            // Check if the cart has any items
            if (!cartCriteria.list().isEmpty()) {
                List<Cart> cartList = cartCriteria.list();

                JsonArray cartArray = new JsonArray(); // Array to hold all cart items with product details

                for (Cart cart : cartList) {
                    JsonObject cartObject = new JsonObject();
cart.setUser(null);
                    // Fetch the product for each cart item
                    Criteria productCriteria = session.createCriteria(Product.class);
                    productCriteria.add(Restrictions.eq("id", cart.getProduct().getId()));
                    Product product = (Product) productCriteria.uniqueResult();

                    if (product != null) {
                        product.setUserEmail(null);
                        // Convert the product to a JSON object
                        JsonObject productObject = gson.toJsonTree(product).getAsJsonObject();

                        // Fetch associated product images
                        Criteria productImgCriteria = session.createCriteria(ProductImg.class);
                        productImgCriteria.add(Restrictions.eq("product", product));
                        List<ProductImg> productImgList = productImgCriteria.list();
                    
                        // Add product images if they exist
                        if (!productImgList.isEmpty()) {
                            for (ProductImg productImg : productImgList) {
                                productImg.setProduct(null); // Avoid circular reference in JSON
                            }
                            productObject.add("productImgs", gson.toJsonTree(productImgList)); // Attach images to the product object
                        }

                        // Add product details to the cart object
                        cartObject.add("product", productObject);
                        cartObject.addProperty("quantity", cart.getQty()); // Add quantity to the cart object
                        cartObject.addProperty("cartId", cart.getId()); // Add cart ID if needed

                        // Add the cart object to the array
                        cartArray.add(cartObject);
                    }
                }

                // If we found cart items, set success to true and add the cart array to the response
                jsonObject.addProperty("success", true);
                jsonObject.add("cartList", cartArray);
            }
        } else {
            System.out.println("User not found.");
        }

        // Write the response
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));

    } catch (Exception e) {
        e.printStackTrace();
        jsonObject.addProperty("error", e.getMessage());
        response.getWriter().write(gson.toJson(jsonObject));
    } finally {
        // Ensure the session is closed after use
        if (session != null) {
            session.close();
        }
    }
}


}
