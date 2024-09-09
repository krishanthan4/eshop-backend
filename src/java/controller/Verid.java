package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Brand;
import entity.Category;
import entity.CategoryHasBrand;
import entity.ModelHasBrand;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import entity.Model;

@WebServlet("/Verid")
public class Verid extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_DTO responsedto = new Response_DTO();
        Gson gson = new Gson();
        Session session = null;
        Transaction tx = null;
        try {
             session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

      
               Criteria categoryCriteria = session.createCriteria(Category.class);
List<Category> categoryList = categoryCriteria.list();

// Create a JSON array for the response
JsonArray categoryArray = new JsonArray();

for (Category category : categoryList) {
    JsonObject categoryJson = new JsonObject();
    categoryJson.addProperty("catId", category.getId());
    categoryJson.addProperty("catName", category.getCatName());
    categoryJson.addProperty("catIcon", category.getCatIcon()); // You can set the actual icon path here.

    // Fetch CategoryHasBrand entries associated with the current category
    Criteria categoryHasBrandCriteria = session.createCriteria(CategoryHasBrand.class)
            .add(Restrictions.eq("categoryCatId", category.getId())); // Assuming categoryId is the field
    List<CategoryHasBrand> categoryHasBrandList = categoryHasBrandCriteria.list();

    JsonArray brandArray = new JsonArray();

    for (CategoryHasBrand categoryHasBrand : categoryHasBrandList) {
        // Fetch the brand associated with this CategoryHasBrand
        Brand brand = (Brand) session.get(Brand.class, categoryHasBrand.getBrandBrand()); // Use the ID to get the brand

        JsonObject brandJson = new JsonObject();
        brandJson.addProperty("brandId", brand.getId());
        brandJson.addProperty("brandName", brand.getBrandName());

        // Fetch ModelHasBrand entries associated with the current brand
        Criteria modelHasBrandCriteria = session.createCriteria(ModelHasBrand.class)
                .add(Restrictions.eq("brandBrandId", brand.getId())); // Assuming brandBrand is the field
        List<ModelHasBrand> modelHasBrandList = modelHasBrandCriteria.list();

        JsonArray modelArray = new JsonArray();

        for (ModelHasBrand modelHasBrand : modelHasBrandList) {
            // Fetch the model associated with this ModelHasBrand
            Model model = (Model) session.get(Model.class, modelHasBrand.getModelModel().getId());

            JsonObject modelJson = new JsonObject();
            modelJson.addProperty("modelId", model.getId());
            modelJson.addProperty("modelName", model.getModelName());

            // Fetch products associated with this model
            Criteria productCriteria = session.createCriteria(Product.class)
                    .add(Restrictions.eq("modelHasBrand", modelHasBrand.getId())); // Assuming modelHasBrand is the field
            List<Product> productList = productCriteria.list();

            JsonArray productArray = new JsonArray();

            for (Product product : productList) {
                JsonObject productJson = new JsonObject();
                productJson.addProperty("id", product.getId());
                productJson.addProperty("title", product.getTitle());
                productArray.add(productJson);
            }

            modelJson.add("products", productArray);
            modelArray.add(modelJson);
        }

        brandJson.add("models", modelArray);
        brandArray.add(brandJson);
    }

    categoryJson.add("brands", brandArray);
    categoryArray.add(categoryJson);
}

// Send response
JsonObject responseObject = new JsonObject();
responseObject.add("categories", categoryArray);

            // Commit the transaction if required
            tx.commit();

            // Send the response back as JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(responseObject));

        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }

    }

}
