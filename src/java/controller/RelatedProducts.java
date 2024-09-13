import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.Product;
import entity.ProductImg;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import model.HibernateUtil;
import org.hibernate.criterion.Order;

@WebServlet("/RelatedProducts")
public class RelatedProducts extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);
        
        Session session = null;

        try {
            // Parse product ID from the request

            session = HibernateUtil.getSessionFactory().openSession();
            
          Criteria criteria2 = session.createCriteria(Product.class);
criteria2.addOrder(Order.asc("datetimeAdded"));
criteria2.setMaxResults(6);
List<Product> productList = criteria2.list();

if (!productList.isEmpty()) {
    jsonObject.addProperty("success", true);

    JsonArray productArray = new JsonArray(); // Create an array to hold product objects with their images

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

    // Add the product array to the response JSON
    jsonObject.add("productList", productArray);
}    
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close(); // Ensure the session is always closed
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(jsonObject));
        }
    }
}
