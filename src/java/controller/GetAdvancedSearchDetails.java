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
import entity.ProductImg;
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

@WebServlet("/GetAdvancedSearchDetails")
public class GetAdvancedSearchDetails extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        Session session = null;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);
           JsonArray productArray=null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria1 = session.createCriteria(Category.class);
            List<Category> categoryList = criteria1.list();
            if (!categoryList.isEmpty()) {
                for (Category category : categoryList) {
                    category.setCatIcon(null);
                    category.setCatImg(null);
                }
            }
                
Criteria criteria3 = session.createCriteria(Product.class);
criteria3.addOrder(Order.asc("datetimeAdded"));
List<Product> productList = criteria3.list();

if (!productList.isEmpty()) {
    jsonObject.addProperty("success", true);

     productArray = new JsonArray(); // Create an array to hold product objects with their images

    for (Product product : productList) {
        // Convert the product object to a JsonObject
product.setUserEmail(null);

        JsonObject productObject = gson.toJsonTree(product).getAsJsonObject();
        // Query to get product images for this product
        Criteria productImgCriteria = session.createCriteria(ProductImg.class);
        productImgCriteria.add(Restrictions.eq("product", product)); // Assuming `ProductImg` has a field that references `Product`
        List<ProductImg> productImgList = productImgCriteria.list();

        // Add images to the product object if any exist
        if (!productImgList.isEmpty()) {
            for (ProductImg productImg : productImgList) {
             productImg.setProduct(null);
            }
            productObject.add("productImgs", gson.toJsonTree(productImgList)); // Attach images directly to the product object
        }

        // Add the complete product (with images) to the product array
        productArray.add(productObject);
    }
}

            session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria2 = session.createCriteria(Color.class);
            List<Color> colorList = criteria2.list();
            if (!categoryList.isEmpty()) {
                jsonObject.addProperty("success", true);
                jsonObject.add("categoryList", gson.toJsonTree(categoryList));
                jsonObject.add("colorList", gson.toJsonTree(colorList));
                jsonObject.add("productList", gson.toJsonTree(productArray));
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
