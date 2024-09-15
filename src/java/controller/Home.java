package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Category;
import entity.Model;
import entity.Product;
import entity.ProductImg;
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

@WebServlet("/Home")
public class Home extends HttpServlet {

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Gson gson = new Gson();
    Session session = null;

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("success", false);

    try {
        session = HibernateUtil.getSessionFactory().openSession();

        // Create an array to hold categories
        JsonArray categoryArray = new JsonArray();

        Criteria categoryCriteria = session.createCriteria(Category.class);
        List<Category> categoryList = categoryCriteria.list();

        if (!categoryList.isEmpty()) {
            for (Category category : categoryList) {
                JsonObject categoryJson = new JsonObject();
                categoryJson.addProperty("categoryId", category.getId());
                categoryJson.addProperty("categoryName", category.getCatName());
                categoryJson.addProperty("categoryIcon", category.getCatIcon());
                categoryJson.addProperty("categoryImg", category.getCatImg());

                // For each category, get its models
                Criteria modelCriteria = session.createCriteria(Model.class);
                modelCriteria.add(Restrictions.eq("categoryCatId", category));
                List<Model> modelList = modelCriteria.list();

                if (!modelList.isEmpty()) {
                    JsonArray modelArray = new JsonArray(); // Array to hold models for this category

                    for (Model model : modelList) {
                        JsonObject modelJson = new JsonObject();
                                              modelJson.addProperty("modelId", model.getId());
                        modelJson.addProperty("modelName", model.getModelName());

                        // For each model, get its products
                        Criteria productCriteria = session.createCriteria(Product.class);
                        productCriteria.add(Restrictions.eq("model", model));
                        List<Product> productList = productCriteria.list();

                        if (!productList.isEmpty()) {
                            JsonArray productArray = new JsonArray(); // Array to hold products for this model

                            for (Product product : productList) {
                                JsonObject productJson = gson.toJsonTree(product).getAsJsonObject();
                                productJson.remove("userEmail"); // Remove sensitive fields
//                                productJson.remove("id"); // Remove sensitive fields
                                productJson.remove("price"); // Remove sensitive fields
                                productJson.remove("description"); // Remove sensitive fields
                                productJson.remove("model"); // Remove sensitive fields
                                productJson.remove("color"); // Remove sensitive fields
                                productJson.remove("condition"); // Remove sensitive fields
                                productJson.remove("qty"); // Remove sensitive fields
                                productJson.remove("datetimeAdded"); // Remove sensitive fields
                                productJson.remove("deliveryFee"); // Remove sensitive fields
                                productJson.remove("status"); // Remove sensitive fields

                                // Get product images for this product
               
                                productArray.add(productJson); // Add product to the product array
                            }

                            modelJson.add("products", productArray); // Add products to the model
                        }

                        modelArray.add(modelJson); // Add model to the model array
                    }

                    categoryJson.add("models", modelArray); // Add models to the category
                }

                categoryArray.add(categoryJson); // Add category to the category array
            }

            jsonObject.add("categoryList", categoryArray); // Add category array to the final JSON response
            jsonObject.addProperty("success", true);
        }

        // Separate product list not tied to categories
        Criteria productCriteria = session.createCriteria(Product.class);
        productCriteria.addOrder(Order.asc("datetimeAdded"));
        List<Product> allProductList = productCriteria.list();

        if (!allProductList.isEmpty()) {
            JsonArray productArray = new JsonArray();

            for (Product product : allProductList) {
                JsonObject productJson = gson.toJsonTree(product).getAsJsonObject();
                productJson.remove("userEmail");

                // Fetch and attach images for each product
                Criteria productImgCriteria = session.createCriteria(ProductImg.class);
                productImgCriteria.add(Restrictions.eq("product", product));
                List<ProductImg> productImgList = productImgCriteria.list();

                if (!productImgList.isEmpty()) {
                    for (ProductImg productImg : productImgList) {
                        productImg.setProduct(null);
                    }
                    
                    productJson.add("productImgs", gson.toJsonTree(productImgList));
                }

                productArray.add(productJson);
            }

            jsonObject.add("productList", productArray); // Add the separate product list
        }

    } catch (Exception e) {
        System.out.println("Error : " + e);
    } finally {
        if (session != null) {
            session.close();
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));
        System.out.println("Categories :" + gson.toJson(jsonObject));
    }
}


}
