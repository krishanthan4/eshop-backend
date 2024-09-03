package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;

@WebServlet("/Signup")
public class SignUp extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        try {
             JsonObject jsonObject = gson.fromJson(request.getReader(),JsonObject.class);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        
response.getWriter().print(gson.toJson(jsonObject));
        System.out.println(jsonObject);
        } catch (JsonSyntaxException e) {
            
        response.getWriter().print("");
        }
        
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
    
    
}
