package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Brand;
import entity.Model;
import entity.Category;
import entity.Condition;
import entity.Gender;
import entity.ModelHasBrand;
import entity.Product;
import entity.Status;
import entity.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import java.util.List;
import org.hibernate.Transaction;

@WebServlet("/Home")
public class Home extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Gson gson = new Gson();
//        Session session = null;
//        Transaction tx = null;
//
//        try {
//            // Open a new session for interacting with the database
//            session = HibernateUtil.getSessionFactory().openSession();
//            tx = session.beginTransaction(); // Optional if you're only reading data, not writing.
//
//            Criteria categoryCriteria = session.createCriteria(Category.class);
//            Criteria brandCriteria = session.createCriteria(Brand.class);
//            Criteria modelCriteria = session.createCriteria(Model.class);
//            Criteria productCriteria = session.createCriteria(Product.class);
//
//            List<Category> categoryList = categoryCriteria.list();
//            List<Brand> brandList = brandCriteria.list();
//            List<Model> modelList = modelCriteria.list();
//            List<Product> productList = productCriteria.list();
//
//            // Create a JSON object to store all the data
//            JsonObject responseObject = new JsonObject();
//
//            // Add the lists to the JSON object
//            responseObject.add("categories", gson.toJsonTree(categoryList));
//            responseObject.add("brands", gson.toJsonTree(brandList));
//            responseObject.add("models", gson.toJsonTree(modelList));
//            responseObject.add("products", gson.toJsonTree(productList));
//
//            // Commit the transaction if required (optional for read-only operations)
//            tx.commit();
//
//            // Set response type as JSON and send the response back to the front end
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().write(gson.toJson(responseObject));
//            System.out.println(gson.toJson(responseObject));
//
//        } catch (Exception e) {
//            if (tx != null) {
//                tx.rollback(); // Rollback if any error occurs during the transaction
//            }
//Response_DTO responsedto = new Response_DTO();
//responsedto.setContent("Server Error");
//
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().write(gson.toJson(responsedto));
//            System.err.println("Error during database interaction: " + e.getMessage());
//        } finally {
//            if (session != null) {
//                session.close(); // Ensure the session is always closed
//            }
//        }
    }

}
