/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Cart_DTO;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


@WebServlet("/AddToCart")
public class AddToCart extends HttpServlet {

 @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Gson gson = new Gson();
    org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
    Transaction transaction = session.beginTransaction();
    Response_DTO response_DTO = new Response_DTO();
    try {
        JsonObject requestObject = gson.fromJson(request.getReader(), JsonObject.class);
        String id = requestObject.get("productId").getAsString();
        String qty = requestObject.get("qty").getAsString();

        if (!Validations.isInteger(id)) {
            response_DTO.setContent("Product not found");
        } else if (!Validations.isInteger(qty)) {
            response_DTO.setContent("Invalid Quantity");
        } else {
            int productId = Integer.parseInt(id);
            int productQty = Integer.parseInt(qty);

            if (productQty <= 0) {
                response_DTO.setContent("Quantity must be greater than 0");
            } else {
                Product product = (Product) session.get(Product.class, productId);
                if (product != null) {
                    if (request.getSession().getAttribute("user") != null) {
                        String userEmail = (String) request.getSession().getAttribute("user");

                        // Get db user
                        Criteria criteria1 = session.createCriteria(User.class);
                        criteria1.add(Restrictions.eq("email", userEmail));
                        User user = (User) criteria1.uniqueResult();

                        // Check in db cart
                        Criteria criteria2 = session.createCriteria(Cart.class);
                        criteria2.add(Restrictions.eq("user", user));
                        criteria2.add(Restrictions.eq("product", product));

                        if (criteria2.list().isEmpty()) {
                            // Item not in cart
                            if (productQty <= product.getQty()) {
                                Cart cart = new Cart();
                                cart.setProduct(product);
                                cart.setQty(productQty);
                                cart.setUser(user);
                                session.save(cart);
                                transaction.commit();
                                response_DTO.setSuccess(true);
                                response_DTO.setContent("Product Added to the Cart");
                            } else {
                                response_DTO.setContent("Invalid Quantity");
                            }
                        } else {
                            // Item already in cart
                            Cart cartItem = (Cart) criteria2.uniqueResult();
                            if ((cartItem.getQty() + productQty) <= product.getQty()) {
                                cartItem.setQty(cartItem.getQty() + productQty);
                                session.update(cartItem);
                                transaction.commit();
                                response_DTO.setSuccess(true);
                                response_DTO.setContent("Cart item updated");
                            } else {
                                response_DTO.setContent("Quantity not available");
                            }
                        }
                    } else {
                        // Handle session cart logic (unchanged)
                        
                               //Session Cart

                            HttpSession httpSession = request.getSession();

                            if (httpSession.getAttribute("sessionCart") != null) {
//                            session cart found
                                ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) httpSession.getAttribute("sessionCart");

                                Cart_DTO foundCart_DTO = null;

                                for (Cart_DTO cart_DTO : sessionCart) {

                                    if (cart_DTO.getProduct().getId() == product.getId()) {
                                        foundCart_DTO = cart_DTO;
                                        break;
                                    }
                                }
                                if (foundCart_DTO != null) {
                                    //product found

                                    if ((foundCart_DTO.getQty() + productQty) <= product.getQty()) {
                                        //update quantity
                                        foundCart_DTO.setQty(foundCart_DTO.getQty() + productQty);
                                        response_DTO.setSuccess(true);
                                        response_DTO.setContent("Product Added to the Cart");
                                        
                                         response_DTO.setSuccess(true);
                                    response_DTO.setContent("cart item updated");
                                    } else {
                                        //quantity not available
                                        response_DTO.setContent("quantity not available");
                                    }

                                } else {
                                    //product not found
                                    if (productQty <= product.getQty()) {
                                        //add to session cart
                                        Cart_DTO cart_DTO = new Cart_DTO();
                                        cart_DTO.setProduct(product);
                                        cart_DTO.setQty(productQty);
                                        sessionCart.add(cart_DTO);
                                        
                                        response_DTO.setSuccess(true);
                                        response_DTO.setContent("Product Added to the Cart");

                                    } else {
                                        //quantity not available
                                        response_DTO.setContent("quantity not available");
                                    }

                                }
                            } else {
                                //session cart not found

                                if (productQty <= product.getQty()) {
                                    //add to session cart

                                    ArrayList<Cart_DTO> sessionCart = new ArrayList<>();

                                    Cart_DTO cart_DTO = new Cart_DTO();
                                    cart_DTO.setProduct(product);
                                    cart_DTO.setQty(productQty);
                                    sessionCart.add(cart_DTO);

                                    httpSession.setAttribute("sessionCart", sessionCart);
                                    
                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("Product Added to the Cart");

                                } else {
                                    //quantity not available
                                    response_DTO.setContent("quantity not available");
                                }
                            }
                    }
                } else {
                    response_DTO.setContent("Product not found");
                }
            }
        }
    } catch (Exception e) {
        if (transaction != null) {
            transaction.rollback();
        }
        e.printStackTrace();
        response_DTO.setContent("Unable to process request");
    } finally {
        session.close();
    }
    response.setContentType("application/json");
    response.getWriter().write(gson.toJson(response_DTO));
}

}
