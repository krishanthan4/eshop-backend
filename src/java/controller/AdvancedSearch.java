package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.Product;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import entity.Category;
import entity.Color;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import entity.Model;
import entity.Condition;
import entity.ProductImg;
import org.hibernate.criterion.Order;

@WebServlet("/AdvancedSearch")
public class AdvancedSearch extends HttpServlet {

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Gson gson = new Gson();
    JsonObject responseJsonObject = new JsonObject();
    responseJsonObject.addProperty("success", false);
           JsonArray productArray=null;

    JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);
    Session session = HibernateUtil.getSessionFactory().openSession();

    try {
        Criteria criteria1 = session.createCriteria(Product.class);
criteria1.addOrder(Order.asc("datetimeAdded"));

        // Add Category filter
        if (requestJsonObject.has("category_name")) {
            String category_name = requestJsonObject.get("category_name").getAsString();
            Criteria criteria2 = session.createCriteria(Category.class);
            criteria2.add(Restrictions.eq("catName", category_name));
            Category category = (Category) criteria2.uniqueResult();

            if (category != null) {
                Criteria criteria3 = session.createCriteria(Model.class);
                criteria3.add(Restrictions.eq("categoryCatId", category));
                List<Model> modelList = criteria3.list();
                criteria1.add(Restrictions.in("model", modelList));
            }
        }

        // Add Condition filter
        if (requestJsonObject.has("condition_name")) {
            String condition_name = requestJsonObject.get("condition_name").getAsString();
            Criteria criteria4 = session.createCriteria(Condition.class);
            criteria4.add(Restrictions.eq("conditionName", condition_name));
            Condition product_Condition = (Condition) criteria4.uniqueResult();

            if (product_Condition != null) {
                criteria1.add(Restrictions.eq("condition", product_Condition));
            }
        }

        // Add Color filter
        if (requestJsonObject.has("color_name")) {
            String color_name = requestJsonObject.get("color_name").getAsString();
            Criteria criteria5 = session.createCriteria(Color.class);
            criteria5.add(Restrictions.eq("clrName", color_name));
            Color color = (Color) criteria5.uniqueResult();

            if (color != null) {
                criteria1.add(Restrictions.eq("color", color));
            }
        }

        // Price range filter
        if (requestJsonObject.has("price_range_start") && requestJsonObject.has("price_range_end")) {
            double price_range_start = requestJsonObject.get("price_range_start").getAsDouble();
            double price_range_end = requestJsonObject.get("price_range_end").getAsDouble();

            criteria1.add(Restrictions.ge("price", price_range_start));
            criteria1.add(Restrictions.le("price", price_range_end));
        }

        // Sorting
        if (requestJsonObject.has("sort_text")) {
            String sort_text = requestJsonObject.get("sort_text").getAsString();
            if (sort_text.equals("Sort by Latest")) {
                criteria1.addOrder(Order.desc("id"));
            } else if (sort_text.equals("Sort by Oldest")) {
                criteria1.addOrder(Order.asc("id"));
            } else if (sort_text.equals("Sort by Name")) {
                criteria1.addOrder(Order.asc("title")); // Corrected to ascending order
            } else if (sort_text.equals("Sort by Price")) {
                criteria1.addOrder(Order.asc("price")); // Corrected to ascending order
            } else {
                criteria1.addOrder(Order.desc("id")); // Default sorting
            }
        }

        // Count all products
        responseJsonObject.addProperty("allProductCount", String.valueOf(criteria1.list().size()));

//        // Pagination
List<Product> productList = criteria1.list();

if (!productList.isEmpty()) {
    responseJsonObject.addProperty("success", true);

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



        // Remove sensitive information
        for (Product product : productList) {
            product.setUserEmail(null);
        }

        // Add product list to response
        responseJsonObject.add("productList", gson.toJsonTree(productArray));
        responseJsonObject.addProperty("success", true);

        // Send response
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));
        System.out.println(gson.toJson(responseJsonObject));
    } finally {
        session.close();
    }
}

}
