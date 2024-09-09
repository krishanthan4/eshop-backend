package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Brand;
import entity.Category;
import entity.CategoryHasBrand;
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
import org.hibernate.criterion.Restrictions;

@WebServlet("/GetBrandsForCategories")
public class GetBrandsForCategories extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        Session session = null;
        Response_DTO responsedto = new Response_DTO();

        try {

            JsonObject categoryJson = gson.fromJson(request.getReader(), JsonObject.class);
            String catId = categoryJson.get("catId").getAsString();

            session = HibernateUtil.getSessionFactory().openSession();
            Criteria categoryHasBrandCriteria = session.createCriteria(CategoryHasBrand.class);
            
            JsonArray categoryHasBrandArray = new JsonArray();

            if (!categoryHasBrandCriteria.list().isEmpty()) {
                List<CategoryHasBrand> categoryHasBrandList = categoryHasBrandCriteria.list();
                for (CategoryHasBrand categoryHasBrand : categoryHasBrandList) {
//                    categoryObject.addProperty("categoryCatId", categoryHasBrand.getId());
//                    categoryObject.addProperty("brandBrandid", category.getCatName());

                    Criteria brandCriteria = session.createCriteria(Brand.class);
                    brandCriteria.add(Restrictions.eq("categoryCatId", catId));
                    if (!brandCriteria.list().isEmpty()) {
                        List<Brand> brandList = brandCriteria.list();
                        for (Brand brand : brandList) {
                            JsonObject brandObject = new JsonObject();
                            brandObject.addProperty("brandId", brand.getId());
                            brandObject.addProperty("brandName", brand.getBrandName());
categoryHasBrandArray.add(brandObject);
                        }
                    }
                }
            }

            response.getWriter().write(gson.toJson(categoryHasBrandArray));

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
