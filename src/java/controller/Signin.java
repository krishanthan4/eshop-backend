package controller;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;

@WebServlet("/Signin")
public class Signin extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        User user = gson.fromJson(request.getReader(),User.class);
        
        
response.getWriter().print(gson.toJson(user));
        System.out.println(user.getEmail() +" "+user.getPassword() );
    }
    
}
