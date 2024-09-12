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

    try {
        
JsonObject requestObject = gson.fromJson(request.getReader(), JsonObject.class);
String cartProductId=  requestObject.get("cartProductId").getAsString();
        // Open session for database interaction
        session = HibernateUtil.getSessionFactory().openSession();
    User_DTO user_DTO = new User_DTO();
    user_DTO.setEmail((String) httpSession.getAttribute("user"));
            ArrayList<Cart_DTO> cart_DTO_List = new ArrayList<>();

        // Fetch the user from the session (replace "" with actual user email from the session or request)
//        String userEmail = ;
        Criteria userCriteria = session.createCriteria(User.class);
        userCriteria.add(Restrictions.eq("email", user_DTO.getEmail()));
        User user = (User) userCriteria.uniqueResult();

        if (user != null) {
        Criteria cartCriteria = session.createCriteria(Cart.class);
cartCriteria.add(Restrictions.eq("user", user)); // Match user
cartCriteria.add(Restrictions.eq("productId", cartProductId)); // Match productId

// Fetch the Cart entity (assume there's only one match)
Cart cartItem = (Cart) cartCriteria.uniqueResult();

// If the item exists, delete it
if (cartItem != null) {
    session.delete(cartItem);
    session.getTransaction().commit(); // Commit the transaction
    System.out.println("Cart item removed successfully.");
} else {
    System.out.println("Cart item not found.");
}

                 // Write the response
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));
        } else {
// Session Cart
if (httpSession.getAttribute("sessionCart") != null) {
    cart_DTO_List = (ArrayList<Cart_DTO>) httpSession.getAttribute("sessionCart");

    // Create the root JsonObject for the response
    JsonObject jsonObject2 = new JsonObject();
    jsonObject2.addProperty("success", true);

    // Create a JsonArray to store all cart items
    JsonArray cartArray = new JsonArray();

    for (Cart_DTO cart_DTO : cart_DTO_List) {
        // Nullify sensitive fields
        cart_DTO.getProduct().setUserEmail(null);

        // Create a JsonObject for the cart
        JsonObject cartObject = new JsonObject();
        cartObject.addProperty("quantity", cart_DTO.getQty());  // Add cart quantity
        cartObject.addProperty("cartId", cart_DTO.getCartId());  // Add cart ID

        // Create a JsonObject for the product
        Product product = cart_DTO.getProduct();
        JsonObject productObject = new JsonObject();
        productObject.addProperty("id", product.getId());
        productObject.addProperty("price", product.getPrice());
        productObject.addProperty("qty", product.getQty());
        productObject.addProperty("description", product.getDescription());
        productObject.addProperty("title", product.getTitle());
        productObject.addProperty("datetimeAdded", product.getDatetimeAdded().toString());
        productObject.addProperty("deliveryFee", product.getDeliveryFee());

        // Add product color info
        JsonObject colorObject = new JsonObject();
        colorObject.addProperty("clrId", product.getColor().getClrId());
        colorObject.addProperty("clrName", product.getColor().getClrName());
        productObject.add("color", colorObject);

        // Add product condition info
        JsonObject conditionObject = new JsonObject();
        conditionObject.addProperty("id", product.getCondition().getId());
        conditionObject.addProperty("name", product.getCondition().getName());
        productObject.add("condition", conditionObject);

        // Add product status info
        JsonObject statusObject = new JsonObject();
        statusObject.addProperty("id", product.getStatus().getId());
        statusObject.addProperty("status", product.getStatus().getStatus());
        productObject.add("status", statusObject);

        // Fetch associated product images
        Criteria productImgCriteria = session.createCriteria(ProductImg.class);
        productImgCriteria.add(Restrictions.eq("product", product));
        List<ProductImg> productImgList = productImgCriteria.list();

        // Create a JsonArray to hold product images
        JsonArray productImgArray = new JsonArray();
        for (ProductImg productImg : productImgList) {
            JsonObject productImgObject = new JsonObject();
            productImgObject.addProperty("imgPath", productImg.getImgPath());
            productImgArray.add(productImgObject);
        }
        // Attach the images array to the product object
        productObject.add("productImgs", productImgArray);

        // Attach the product object to the cart object
        cartObject.add("product", productObject);

        // Add the cart object to the cart array
        cartArray.add(cartObject);
    }

    // Add the cart array to the main jsonObject
    jsonObject2.add("cartList", cartArray);

    // Write the final jsonObject to the response
    response.setContentType("application/json");
    response.getWriter().write(gson.toJson(jsonObject2));
    response.getWriter().flush();  // Flush to ensure no extra characters are added

    // Comment out or remove the println statement
    // System.out.println("cart: " + gson.toJson(jsonObject));

} else {
    // Handle the case when session cart is not found
    JsonObject jsonObject2 = new JsonObject();
    jsonObject2.addProperty("success", false);
    jsonObject2.addProperty("message", "No items in the cart");

    // Send the response
    response.setContentType("application/json");
    response.getWriter().write(gson.toJson(jsonObject2));
    response.getWriter().flush();  // Flush to ensure no extra characters are added
}

            }
   

    } catch (Exception e) {
        jsonObject.addProperty("error", e.getMessage());
//        response.getWriter().write(gson.toJson(jsonObject));
        System.out.println("Errors : "+e);
    } finally {
        // Ensure the session is closed after use
        if (session != null) {
            session.close();
        }
    }
    }

}
