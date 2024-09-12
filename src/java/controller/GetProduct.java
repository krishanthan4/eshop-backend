import com.google.gson.Gson;
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

@WebServlet("/GetProduct")
public class GetProduct extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);
        
        Session session = null;

        try {
            // Parse product ID from the request
            JsonObject jsonId = gson.fromJson(request.getReader(), JsonObject.class);
            int productId = jsonId.get("productId").getAsInt();

            session = HibernateUtil.getSessionFactory().openSession();
            
            // Fetch product by ID using Criteria API
            Criteria criteria1 = session.createCriteria(Product.class);
            criteria1.add(Restrictions.eq("id", productId));
            Product product = (Product) criteria1.uniqueResult();
            
            if (product != null && !product.getTitle().isEmpty()) {
                JsonObject productObject = gson.toJsonTree(product).getAsJsonObject();
                product.setUserEmail(null); // Avoid circular reference in JSON

                // Fetch associated product images using Criteria API
                Criteria productImgCriteria = session.createCriteria(ProductImg.class);
                productImgCriteria.add(Restrictions.eq("product", product));
                List<ProductImg> productImgList = productImgCriteria.list();
                
                // Add product images if they exist
                if (!productImgList.isEmpty()) {
                    jsonObject.addProperty("success", true);
                    JsonObject imagesObject = new JsonObject();
                    
                    for (ProductImg productImg : productImgList) {
                        productImg.setProduct(null); // Avoid circular reference in JSON
                        JsonObject imgObject = gson.toJsonTree(productImg).getAsJsonObject();
                        imagesObject.add("imgPath", imgObject); // Add image object to JSON
                    }
                    
                    productObject.add("images", imagesObject);
                }

                jsonObject.add("product", productObject);
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
