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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet("/GetCart")
public class GetCart extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        Session session = null;
        HttpSession httpSession = request.getSession();

        try {
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
                // Fetch the cart items for the logged-in user
                System.out.println("||User here||" + user.getEmail());
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
                } else {
                    jsonObject.addProperty("success", true);
                    jsonObject.addProperty("content", "noItems");

                }
                // Write the response
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(jsonObject));
            } else {
// Session Cart
                if (httpSession.getAttribute("sessionCart") != null) {
                    cart_DTO_List = (ArrayList<Cart_DTO>) httpSession.getAttribute("sessionCart");
                    System.out.println("||SessionCart here||" + gson.toJsonTree(cart_DTO_List));

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
                    jsonObject2.addProperty("success", true);
                    jsonObject2.addProperty("content", "noItems");

                    // Send the response
                    response.setContentType("application/json");
                    response.getWriter().write(gson.toJson(jsonObject2));
                    System.out.println(gson.toJson(jsonObject2));
                    response.getWriter().flush();  // Flush to ensure no extra characters are added
                }

            }

        } catch (Exception e) {
            jsonObject.addProperty("error", e.getMessage());
//        response.getWriter().write(gson.toJson(jsonObject));
            System.out.println("Errors : " + e);
        } finally {
            // Ensure the session is closed after use
            if (session != null) {
                session.close();
            }
        }
    }

}
