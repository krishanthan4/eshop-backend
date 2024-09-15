
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dto.Cart_DTO;
import dto.Response_DTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import model.HibernateUtil;
import entity.User;
import java.util.ArrayList;
import util.config;
import entity.Cart;
import entity.Product;
import org.hibernate.Transaction;

@WebServlet("/Signin")
public class Signin extends HttpServlet {

   @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Response_DTO response_DTO = new Response_DTO();
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    JsonObject userJson = gson.fromJson(request.getReader(), JsonObject.class);

    // Input validation
    String email = userJson.get("email").getAsString();
    String password = userJson.get("password").getAsString();

    if (email.isEmpty()) {
        response_DTO.setContent("Please enter your Email");
    } else if (password.isEmpty()) {
        response_DTO.setContent("Please enter your Password");
    } else {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", email));
            criteria.add(Restrictions.eq("password", password));

            User user = (User) criteria.uniqueResult(); // Avoid multiple list calls, use uniqueResult()

            if (user != null) {
                if (!"verified".equals(user.getVerificationCode())) {
                    request.getSession().setAttribute("user", email);
                    response_DTO.setContent("Unverified");
                } else {
                    // Successful login logic
                    response_DTO.setSuccess(true);
                    response_DTO.setContent("Sign in Success");

                    if (request.getSession().getAttribute("sessionCart") != null) {
                        ArrayList<Cart_DTO> cart_DTO_List = (ArrayList<Cart_DTO>) request.getSession().getAttribute("sessionCart");

                        for (Cart_DTO cart_DTO : cart_DTO_List) {
                            Criteria searchProduct = session.createCriteria(Product.class);
                            searchProduct.add(Restrictions.eq("id", cart_DTO.getProduct().getId()));
                            Product searchedCartProduct = (Product) searchProduct.uniqueResult();

                            if (searchedCartProduct != null) {
                                Criteria searchCart = session.createCriteria(Cart.class);
                                searchCart.add(Restrictions.eq("user", user));
                                searchCart.add(Restrictions.eq("product", searchedCartProduct));

                                if (searchCart.uniqueResult() == null) { // Use uniqueResult to simplify
                                    Cart cart = new Cart();
                                    cart.setProduct(searchedCartProduct);
                                    cart.setQty(cart_DTO.getQty());
                                    cart.setUser(user);
                                    session.save(cart);
                                                                            System.out.println(":::: Set sessioncart to User ::::"+cart.getProduct().getTitle() + cart.getQty() + cart.getUser());

                                }
                            }
                        }
                    }
                
                    request.removeAttribute("sessionCart");
                    // Store user information in session
                    request.getSession().setAttribute("user", email);
                }
            } else {
                response_DTO.setContent("Invalid Details! Please try again");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            response_DTO.setContent("Error processing request. Please try again later.");
        } finally {
            session.close(); // Ensure session is closed to prevent resource leaks
        }
    }

    // Write response
    response.setContentType("application/json");
    response.getWriter().write(gson.toJson(response_DTO));
    System.out.println(gson.toJson(response_DTO));
}

}
