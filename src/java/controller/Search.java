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

@WebServlet("/Search")
public class Search extends HttpServlet {
    private final Gson gson = new Gson();
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("success", false); // Default response is failure
    JsonObject responseJson = gson.fromJson(request.getReader(), JsonObject.class);
    Session session = null;

    try {
        // Parse the 'text' parameter from the request JSON
        String text = responseJson.get("text").getAsString(); // Use getAsString() to avoid toString() issues

        session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria2 = session.createCriteria(Product.class);
        criteria2.add(Restrictions.or(
            Restrictions.like("title", "%" + text + "%"),  // Added wildcard for LIKE operation
            Restrictions.like("description", "%" + text + "%")  // Added wildcard for LIKE operation
        ));

        criteria2.addOrder(Order.asc("datetimeAdded")); // Sort by datetime
        List<Product> productList = criteria2.list();

        if (!productList.isEmpty()) {
            jsonObject.addProperty("success", true);

            JsonArray productArray = new JsonArray(); // Array to hold products and their images

            for (Product product : productList) {
                // Exclude sensitive user information from the response
                product.setUserEmail(null);

                // Convert product object to JSON
                JsonObject productObject = gson.toJsonTree(product).getAsJsonObject();

                // Query product images for the current product
                Criteria productImgCriteria = session.createCriteria(ProductImg.class);
                productImgCriteria.add(Restrictions.eq("product", product)); // Assuming `ProductImg` has a `product` field
                List<ProductImg> productImgList = productImgCriteria.list();

                // Attach images to the product object if any exist
                if (!productImgList.isEmpty()) {
                    for (ProductImg productImg : productImgList) {
                        productImg.setProduct(null); // Prevent infinite loops in serialization
                    }
                    productObject.add("productImgs", gson.toJsonTree(productImgList)); // Attach images to product
                }

                productArray.add(productObject); // Add product with images to the array
            }

            // Add the complete product array to the response
            jsonObject.add("productList", productArray);
        } else {
            jsonObject.addProperty("content", "noProduct");
        }

    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
        jsonObject.addProperty("error", "An error occurred while processing the request.");
    } finally {
        if (session != null) {
            session.close(); // Ensure session is closed
        }

        // Prepare and send the response
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));
    }
}

}
