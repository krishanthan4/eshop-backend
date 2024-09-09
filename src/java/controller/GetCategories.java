package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Category;
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

@WebServlet("/GetCategories")
public class GetCategories extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        Session session = null;
        Response_DTO responsedto = new Response_DTO();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria categoryCriteria = session.createCriteria(Category.class);
            JsonArray categoryArray = new JsonArray();

            if (!categoryCriteria.list().isEmpty()) {
                List<Category> categoryList = categoryCriteria.list();
                for (Category category : categoryList) {
                    JsonObject categoryObject = new JsonObject();
                    categoryObject.addProperty("catId", category.getId());
                    categoryObject.addProperty("catName", category.getCatName());
                    categoryObject.addProperty("catIcon", category.getCatIcon());
                    categoryObject.addProperty("catImg", category.getCatImg());
                    categoryArray.add(categoryObject);
                }
            }

            response.getWriter().write(gson.toJson(categoryArray));

        } catch (Exception e) {
            System.out.println("Error : " + e);
            responsedto.setContent("Something went wrong");
            response.getWriter().write(gson.toJson(responsedto));
        } finally {
            if (session != null) {
                session.close(); // Ensure the session is always closed
            }
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

    }

}
