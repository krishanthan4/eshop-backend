package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
import dto.Cart_DTO;
import entity.Address;
import entity.Cart;
import entity.Category;
import entity.City;
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

@WebServlet("/GetCart")
public class GetCart extends HttpServlet {
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    Gson gson = new Gson();
    JsonObject jsonObject = new JsonObject();
    HttpSession httpSession = request.getSession();
    Session session = HibernateUtil.getSessionFactory().openSession();
    User_DTO user_DTO = new User_DTO();
    user_DTO.setEmail((String) httpSession.getAttribute("user"));
    ArrayList<Cart_DTO> cart_DTO_List = new ArrayList<>();

    try {
        // Ensure fresh data from database, disable cache
        Criteria userCriteria = session.createCriteria(User.class);
        userCriteria.add(Restrictions.eq("email", user_DTO.getEmail()));
        userCriteria.setCacheable(false); // Disable Hibernate caching for this query
        User user = (User) userCriteria.uniqueResult();

        if (user != null) {
               // Fetch the cart items for the logged-in user
               Transaction transaction = session.beginTransaction();
                System.out.println("||User here||" + user.getEmail());
                Criteria cartCriteria = session.createCriteria(Cart.class);
                cartCriteria.add(Restrictions.eq("user", user));
transaction.commit();

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
                } else {
                    jsonObject.addProperty("success", true);
                    jsonObject.addProperty("content", "noItems");

                }
                // Write the response
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(jsonObject));
        } else {
            if (httpSession.getAttribute("sessionCart") != null) {
                cart_DTO_List = (ArrayList<Cart_DTO>) httpSession.getAttribute("sessionCart");
                System.out.println("||SessionCart here||" + gson.toJsonTree(cart_DTO_List));

                JsonObject jsonObject2 = new JsonObject();
                jsonObject2.addProperty("success", true);
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

                    // Fetch and attach product images (same logic as above)
                    Criteria productImgCriteria = session.createCriteria(ProductImg.class);
                    productImgCriteria.add(Restrictions.eq("product", product));
                    productImgCriteria.setCacheable(false);  // Disable cache for product images query
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
            } else {
                JsonObject jsonObject2 = new JsonObject();
                jsonObject2.addProperty("success", true);
                jsonObject2.addProperty("content", "noItems");
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(jsonObject2));
            }
        }

    } catch (Exception e) {
        jsonObject.addProperty("error", e.getMessage());
        System.out.println("Errors: " + e);
    } finally {
        if (session != null) {
            session.close();
        }
    }
}

}
