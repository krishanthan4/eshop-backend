package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import entity.User;
import javax.servlet.http.Cookie;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

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
                    //not verified
                    request.getSession().setAttribute("user", userJson.get("email").getAsString());
                    response_DTO.setSuccess(true);
                    response_DTO.setContent("Unverified");
                } else {
                    //verified
                    request.getSession().setAttribute("user", userJson.get("email").getAsString());

            
                    if (userJson.get("RememberMe").getAsBoolean()) {
                        Cookie userEmailCookie = new Cookie("user_email", userJson.get("email").getAsString());
                         userEmailCookie.setHttpOnly(true);
//                    userEmailCookie.setSecure(true);
                        userEmailCookie.setPath("/");
                        userEmailCookie.setMaxAge(60 * 60 * 24 * 7);
                        response.addCookie(userEmailCookie);
                        Cookie userPasswordCookie = new Cookie("user_password", userJson.get("password").getAsString());
                               userPasswordCookie.setHttpOnly(true);
//                    userPasswordCookie.setSecure(true);
                        userPasswordCookie.setPath("/");
                        userPasswordCookie.setMaxAge(60 * 60 * 24 * 7);
                        response.addCookie(userPasswordCookie);
                    }
                    response_DTO.setSuccess(true);
                    response_DTO.setContent("Sign in Success");

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
