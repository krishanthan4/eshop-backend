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

        // Open session for database interaction
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();  // Start a transaction

        String email2 = (String) httpSession.getAttribute("user");
        ArrayList<Cart_DTO> cart_DTO_List = new ArrayList<>();

        // Fetch the user from the session
        Criteria userCriteria = session.createCriteria(User.class);
        userCriteria.add(Restrictions.eq("email", email2));
        User user = (User) userCriteria.uniqueResult();

        if (user != null) {
            Criteria productCriteria = session.createCriteria(Product.class);
            productCriteria.add(Restrictions.eq("id", cartProductId));
            Product product = (Product) productCriteria.uniqueResult();

            Criteria cartCriteria = session.createCriteria(Cart.class);
            cartCriteria.add(Restrictions.eq("user", user));  // Match user
            cartCriteria.add(Restrictions.eq("product", product));  // Match product

            // Fetch the Cart entity
            Cart cartItem = (Cart) cartCriteria.uniqueResult();

            // If the item exists, delete it
            if (cartItem != null) {
                session.delete(cartItem);
                session.flush();  // Ensure the delete is executed
                transaction.commit();  // Commit the transaction
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
        } else {
            // Handle session cart
            if (httpSession.getAttribute("sessionCart") != null) {
                System.out.println("Session Cart");
                cart_DTO_List = (ArrayList<Cart_DTO>) httpSession.getAttribute("sessionCart");

                JsonObject jsonObject2 = new JsonObject();
                JsonArray cartArray = new JsonArray();

                for (Cart_DTO cart_DTO : cart_DTO_List) {
                    cart_DTO.getProduct().setUserEmail(null);
                    JsonObject cartObject = new JsonObject();
                    cartObject.addProperty("quantity", cart_DTO.getQty());
                    cartObject.addProperty("cartId", cart_DTO.getCartId());

                    Product product = cart_DTO.getProduct();
                    JsonObject productObject = new JsonObject();
                    productObject.addProperty("id", product.getId());
                    productObject.addProperty("price", product.getPrice());
                    productObject.addProperty("qty", product.getQty());
                    productObject.addProperty("description", product.getDescription());
                    productObject.addProperty("title", product.getTitle());
                    productObject.addProperty("datetimeAdded", product.getDatetimeAdded().toString());
                    productObject.addProperty("deliveryFee", product.getDeliveryFee());

                    JsonObject colorObject = new JsonObject();
                    colorObject.addProperty("clrId", product.getColor().getClrId());
                    colorObject.addProperty("clrName", product.getColor().getClrName());
                    productObject.add("color", colorObject);

                    JsonObject conditionObject = new JsonObject();
                    conditionObject.addProperty("id", product.getCondition().getId());
                    conditionObject.addProperty("name", product.getCondition().getName());
                    productObject.add("condition", conditionObject);

                    JsonObject statusObject = new JsonObject();
                    statusObject.addProperty("id", product.getStatus().getId());
                    statusObject.addProperty("status", product.getStatus().getStatus());
                    productObject.add("status", statusObject);

                    Criteria productImgCriteria = session.createCriteria(ProductImg.class);
                    productImgCriteria.add(Restrictions.eq("product", product));
                    List<ProductImg> productImgList = productImgCriteria.list();

                    JsonArray productImgArray = new JsonArray();
                    for (ProductImg productImg : productImgList) {
                        JsonObject productImgObject = new JsonObject();
                        productImgObject.addProperty("imgPath", productImg.getImgPath());
                        productImgArray.add(productImgObject);
                    }
                    productObject.add("productImgs", productImgArray);

                    cartObject.add("product", productObject);
                    cartArray.add(cartObject);
                }

                jsonObject2.add("cartList", cartArray);
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(jsonObject2));
                response.getWriter().flush();

            } else {
                JsonObject jsonObject2 = new JsonObject();
                jsonObject2.addProperty("success", false);
                jsonObject2.addProperty("message", "No items in the cart");

                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(jsonObject2));
                response.getWriter().flush();
            }
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
