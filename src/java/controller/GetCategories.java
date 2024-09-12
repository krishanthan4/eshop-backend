package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Category;
import entity.Color;
import entity.Condition;
import entity.Gender;
import entity.Product;
import entity.Model;
import entity.Status;
import entity.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet("/GetCategories")
public class GetCategories extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        Session session = null;
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria1 = session.createCriteria(Category.class);
            List<Category> categoryList = criteria1.list();

            if(!categoryList.isEmpty()){
        jsonObject.addProperty("success", true);
            jsonObject.add("categoryList", gson.toJsonTree(categoryList));
            }
            
        } catch (Exception e) {
            System.out.println("Error : " + e);
        } finally {
            if (session != null) {
                session.close(); // Ensure the session is always closed
            }
            
                 response.getWriter().write(gson.toJson(jsonObject));
            System.out.println("Categories :" + gson.toJson(jsonObject));
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

    }

}
