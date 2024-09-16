package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.User_DTO;
import entity.Address;
import entity.Cart;
import entity.City;
import entity.InvoiceHasProducts;
import entity.OrderStatus;
import entity.Invoice;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import model.Payhere;
import org.hibernate.HibernateException;
import util.config;

@WebServlet("/Checkout")
public class Checkout extends HttpServlet {
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Gson gson = new Gson();
    HttpSession httpsession = request.getSession();
    Session session = HibernateUtil.getSessionFactory().openSession();
    Transaction transaction = session.beginTransaction();
    JsonObject responseJsonObject = new JsonObject();
    responseJsonObject.addProperty("success", false);

    try {
        // Parse the incoming JSON request
        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        // Check if the user is logged in
        if (httpsession.getAttribute("user") != null) {
            String order_id = requestJsonObject.get("order_id").getAsString();

            // Get the user from the session and database
            String userEmail = httpsession.getAttribute("user").toString();
            Criteria userCriteria = session.createCriteria(User.class);
            userCriteria.add(Restrictions.eq("email", userEmail));
            User user = (User) userCriteria.uniqueResult();

            if (user == null) {
                responseJsonObject.addProperty("content", "User not found.");
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(responseJsonObject));
                return;
            }

            // Retrieve the user's cart items
            Criteria cartCriteria = session.createCriteria(Cart.class);
            cartCriteria.add(Restrictions.eq("user", user));
            List<Cart> cartList = cartCriteria.list();

            Double totalAmount = 0.00;
            if (!cartList.isEmpty()) {
                // Calculate the total amount
                for (Cart cart : cartList) {
                    totalAmount += cart.getProduct().getPrice() * cart.getQty() + cart.getProduct().getDeliveryFee();
                }

                // Create and save an invoice
                Invoice invoice = new Invoice();
                invoice.setOrderId(order_id);
                invoice.setDate(new Date());
                invoice.setUser(user);
                invoice.setTotal(totalAmount);
                session.save(invoice);

                // Verify the invoice was saved
                Criteria invoiceCriteria = session.createCriteria(Invoice.class);
                invoiceCriteria.add(Restrictions.eq("user", user));
                invoiceCriteria.add(Restrictions.eq("invoiceId", invoice.getInvoiceId()));
                Invoice savedInvoice = (Invoice) invoiceCriteria.uniqueResult();

                if (savedInvoice == null) {
                    System.out.println("Invoice could not be retrieved.");
                    responseJsonObject.addProperty("content", "Error processing the order.");
                } else {

                    // Create order items and update product quantities
                    for (Cart cartItem : cartList) {

                        Product product = cartItem.getProduct();
                        InvoiceHasProducts invoiceHasProducts = new InvoiceHasProducts();
                        invoiceHasProducts.setInvoice(invoice);
                        invoiceHasProducts.setProduct(product);
                        invoiceHasProducts.setBoughtQty(cartItem.getQty());
                        invoiceHasProducts.setOrderStatus(1); // Assuming 1 means 'paid'
                        session.save(invoiceHasProducts);

                        // Update product stock
                        product.setQty(product.getQty() - cartItem.getQty());
                        session.update(product);

                        // Remove cart item
                        session.delete(cartItem);
                                            transaction.commit();

                    }

                    // Commit transaction after processing

                    // Add success response
                    responseJsonObject.addProperty("success", true);
                    responseJsonObject.addProperty("content", "Checkout Completed");
                }
            } else {
                responseJsonObject.addProperty("content", "NoCart");
                System.out.println("No cart available for user: " + user.getEmail());
            }
        } else {
            // User not signed in
            responseJsonObject.addProperty("content", "User not signed in.");
        }

        // Send the response as JSON
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));

    } catch (HibernateException e) {
        // Rollback the transaction in case of an exception
        if (transaction != null) {
            transaction.rollback();
        }
        System.out.println("Error in saveOrders method: " + e);
        responseJsonObject.addProperty("content", "Error occurred during checkout.");
    } finally {
        // Ensure session is closed
        if (session != null && session.isOpen()) {
            session.close();
        }
    }
}

}