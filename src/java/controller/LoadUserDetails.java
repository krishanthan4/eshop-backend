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

@WebServlet("/LoadUserDetails")
public class LoadUserDetails extends HttpServlet {
    private final Gson gson = new Gson();
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("success", false);

    Session session = null;

    try {
        session = HibernateUtil.getSessionFactory().openSession();

        // Retrieve user email from session
        String userEmail = (String) request.getSession().getAttribute("user");

        if (userEmail != null) {
            // Fetch user details based on the email
            Criteria userCriteria = session.createCriteria(User.class);
            userCriteria.add(Restrictions.eq("email", userEmail));
            User user = (User) userCriteria.uniqueResult();

            if (user != null) {
                jsonObject.addProperty("firstName", user.getFname());
                jsonObject.addProperty("lastName", user.getLname());
                jsonObject.addProperty("userGenderId", user.getGenderGender().getId());
                jsonObject.addProperty("mobile", user.getMobile());
                jsonObject.addProperty("password", user.getPassword());
                jsonObject.addProperty("joinedDate", user.getJoinedDate().toString());
                jsonObject.addProperty("email", user.getEmail());

                // Fetch user address details
                Criteria addressCriteria = session.createCriteria(Address.class);
                addressCriteria.add(Restrictions.eq("user", user)); // Assuming Address is linked to User
                Address address = (Address) addressCriteria.uniqueResult();

                if (address != null) {
                    jsonObject.addProperty("addressLine1", address.getLine1());
                    jsonObject.addProperty("addressLine2", address.getLine2());
                    jsonObject.addProperty("userProvinceId", address.getCity().getDistrict().getProvince().getProvinceId());
                    jsonObject.addProperty("userDistrictId", address.getCity().getDistrict().getDistrictId());
                    jsonObject.addProperty("userCityId", address.getCity().getCityId());
                    jsonObject.addProperty("userPostalCode", address.getPostalCode());
                }

                // Fetch additional data
                Criteria genderCriteria = session.createCriteria(Gender.class);
                genderCriteria.addOrder(Order.asc("genderName"));
                List<Gender> genderList = genderCriteria.list();
                if (!genderList.isEmpty()) {
                    jsonObject.add("genders", gson.toJsonTree(genderList));
                }

                Criteria provinceCriteria = session.createCriteria(Province.class);
                provinceCriteria.addOrder(Order.asc("provinceName"));
                List<Province> provinceList = provinceCriteria.list();
                if (!provinceList.isEmpty()) {
                    jsonObject.add("provinces", gson.toJsonTree(provinceList));
                                    jsonObject.addProperty("success", true);
                }

                // Set success to true if user details are successfully retrieved
            }
        } else {
            jsonObject.addProperty("error", "User is not authenticated.");
        }

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
