
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.Address;
import entity.City;
import entity.District;
import entity.Gender;
import entity.Product;
import entity.ProductImg;
import entity.Province;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import entity.User;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import model.HibernateUtil;
import org.hibernate.criterion.Order;

@WebServlet("/GetDistricts")
public class GetDistricts extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            // Retrieve user email from session
//            String userEmail = (String) request.getSession().getAttribute("user");
//            if (userEmail != null) {
            JsonObject requestObject = gson.fromJson(request.getReader(), JsonObject.class);
            int provinceId = requestObject.get("provinceId").getAsInt();

            Criteria provinceCriteria = session.createCriteria(Province.class);
            provinceCriteria.add(Restrictions.eq("provinceId", provinceId));
            Province province = (Province) provinceCriteria.uniqueResult();
            System.out.println("province ::" + gson.toJson(province));
            Criteria districtCriteria = session.createCriteria(District.class);
            districtCriteria.add(Restrictions.eq("province", province));
            districtCriteria.addOrder(Order.asc("districtName"));

            List<District> districtList = districtCriteria.list();
            if (!districtList.isEmpty()) {
                jsonObject.add("districts", gson.toJsonTree(districtList));
                jsonObject.addProperty("success", true);
                System.out.println("district ::" + gson.toJson(districtList));
            }

            // Set success to true if user details are successfully retrieved
//            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            jsonObject.addProperty("error", "An error occurred while processing the request.");
        } finally {
            if (session != null) {
                session.close(); // Ensure the session is always closed
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(jsonObject));
            System.out.println(gson.toJson(jsonObject));
        }
    }

}
