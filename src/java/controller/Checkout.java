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
import util.config;

@WebServlet(name = "Checkout", urlPatterns = {"/Checkout"})
public class Checkout extends HttpServlet {

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Gson gson = new Gson();
    JsonObject responseJsonObject = new JsonObject();
    responseJsonObject.addProperty("success", false);

    HttpSession httpSession = request.getSession();
    Session session = HibernateUtil.getSessionFactory().openSession();
    Transaction transaction = session.beginTransaction();

    try {
        if (httpSession.getAttribute("user") != null) {
            // Get user from the database
            String userEmail = httpSession.getAttribute("user").toString();
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("email", userEmail));
            User user = (User) criteria.uniqueResult();

            if (user != null) {
                if (user.getFname() == null || user.getLname() == null || user.getMobile() == null) {
                    responseJsonObject.addProperty("content", "Add Your Name and Mobile to Profile");
                } else {
                    // Get user address
                    Criteria addressCriteria = session.createCriteria(Address.class);
                    addressCriteria.add(Restrictions.eq("user", user));
                    addressCriteria.setMaxResults(1);
                    List<Address> addressList = addressCriteria.list();

                    if (!addressList.isEmpty()) {
                        Address address = addressList.get(0);
                        
                        // Save orders
                        JsonObject responseObject = saveOrders(session, transaction, user, address, responseJsonObject);
                        
                        if (responseObject != null) {
                            responseJsonObject = responseObject; // Update the responseJsonObject
                        } else {
                            System.out.println("saveOrders returned null");
                        }
                    } else {
                        responseJsonObject.addProperty("content", "Add Your Delivery Address to Profile");
                    }
                }
            } else {
                responseJsonObject.addProperty("content", "User not found.");
            }
        } else {
            responseJsonObject.addProperty("content", "User session expired. Please log in again.");
        }
    } catch (Exception e) {
        transaction.rollback();
        System.out.println("Error in doPost method: " + e);
    } finally {
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));

        if (session != null && session.isOpen()) {
            session.close(); // Ensure session is closed
        }
    }
}

private JsonObject saveOrders(Session session, Transaction transaction, User user, Address address, JsonObject responseJsonObject) {
    try {
        Gson gson = new Gson();
        // Get Cart Order
        Criteria criteria = session.createCriteria(Cart.class);
        criteria.add(Restrictions.eq("user", user));
        List<Cart> cartList = criteria.list();
        
        Double totalAmount = 0.00;
        if (!cartList.isEmpty()) {
            // Calculate the total amount
            for (Cart cart : cartList) {
                totalAmount += cart.getProduct().getPrice() * cart.getQty() + cart.getProduct().getDeliveryFee();
            }

            // Create invoice
            Invoice invoice = new Invoice();
            invoice.setOrderId(Validations.generateOrderId());
            invoice.setDate(new Date());
            invoice.setUser(user);
            invoice.setTotal(totalAmount);
            session.save(invoice);

            // Retrieve saved invoice
            Criteria invoiceCriteria = session.createCriteria(Invoice.class);
            invoiceCriteria.add(Restrictions.eq("user", user));
            invoiceCriteria.add(Restrictions.eq("invoiceId", invoice.getInvoiceId()));
            Invoice savedInvoice = (Invoice) invoiceCriteria.uniqueResult();

            StringBuilder items = new StringBuilder();

            // Create order items and update product quantities
            for (Cart cartItem : cartList) {
                items.append(cartItem.getProduct().getTitle()).append(", ");

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
            }

            // Commit transaction
            transaction.commit();

            // Format full address
            String fullAddress = address.getLine1() + ", " + address.getLine2() + ", " + address.getCity().getCityName();

            // Payment Data preparation
            String merchantId = config.MERCHANT_ID;
            String formattedAmount = new DecimalFormat("0.00").format(totalAmount);
            String currency = "LKR";
            String merchantSecret = config.MERCHANT_SECRET;
            String merchantSecretMD5Hash = Payhere.generateMD5(merchantSecret);
            JsonObject payhereObject = new JsonObject();

            payhereObject.addProperty("merchant_id", merchantId);
            payhereObject.addProperty("return_url", "");
            payhereObject.addProperty("cancel_url", "");
            payhereObject.addProperty("notify_url", "");
            payhereObject.addProperty("first_name", user.getFname());
            payhereObject.addProperty("last_name", user.getLname());
            payhereObject.addProperty("email", user.getEmail());
            payhereObject.addProperty("phone", user.getMobile());
            payhereObject.addProperty("address", fullAddress);
            payhereObject.addProperty("city", address.getCity().getCityName());
            payhereObject.addProperty("country", "Sri Lanka");
            payhereObject.addProperty("order_id", String.valueOf(savedInvoice.getOrderId()));
            payhereObject.addProperty("items", items.toString());
            payhereObject.addProperty("currency", currency);
            payhereObject.addProperty("amount", formattedAmount);
            payhereObject.addProperty("sandbox", true);

            String md5Hash = Payhere.generateMD5(merchantId + savedInvoice.getOrderId() + formattedAmount + currency + merchantSecretMD5Hash);
            payhereObject.addProperty("hash", md5Hash);

            // Add payment object to the response
            responseJsonObject.addProperty("success", true);
            responseJsonObject.addProperty("content", "Checkout Completed");
            responseJsonObject.add("payhereList", gson.toJsonTree(payhereObject));
            
            return responseJsonObject;
        } else {
            responseJsonObject.addProperty("content", "NoCart");
            System.out.println("No cart available.");
        }
    } catch (Exception e) {
        transaction.rollback();
        System.out.println("Error in saveOrders method: " + e);
    }
    return responseJsonObject;
}


}
