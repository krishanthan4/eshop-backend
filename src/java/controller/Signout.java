package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Signout")
public class Signout extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
                 request.getSession().invalidate();
                 System.out.println("Session Desctroyed : ");
Response_DTO responsedto = new Response_DTO();
responsedto.setSuccess(true);
Gson gson = new Gson();
response.setContentType("application/json");
response.getWriter().write(gson.toJson(responsedto));
        } catch (Exception e) {
            System.out.println("Error "+e);
        }

    }
    
}
