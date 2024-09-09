import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import javax.servlet.annotation.WebServlet;
import model.HibernateUtil;
import entity.User;
import util.config;

@WebServlet("/Signin")
public class Signin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject userJson = gson.fromJson(request.getReader(), JsonObject.class);

        if (userJson.get("email").getAsString().isEmpty()) {
            response_DTO.setContent("Please enter your Email");
        } else if (userJson.get("password").getAsString().isEmpty()) {
            response_DTO.setContent("Please enter your Password");
        } else {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", userJson.get("email").getAsString()));
            criteria1.add(Restrictions.eq("password", userJson.get("password").getAsString()));

            if (!criteria1.list().isEmpty()) {
                User user = (User) criteria1.list().get(0);

                if (!user.getVerificationCode().equals("verified")) {
                    request.getSession().setAttribute("user", userJson.get("email").getAsString());
                    response_DTO.setContent("Unverified");
                } else {
                    // Generate JWT token
                    String token = Jwts.builder()
                        .setSubject(user.getEmail())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                        .signWith(SignatureAlgorithm.HS512, config.SECRET_KEY.getBytes())
                        .compact();

                    // Send JWT token in response
                    response_DTO.setSuccess(true);
                    response_DTO.setContent("Sign in Success");
                response.addHeader("Authorization", "Bearer " + token);

                    // Optionally, store user information in session
                    request.getSession().setAttribute("user", userJson.get("email").getAsString());
                }
            } else {
                response_DTO.setContent("Invalid Details! Please try again");
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        System.out.println(gson.toJson(response_DTO));
    }
}
