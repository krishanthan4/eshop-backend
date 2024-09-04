package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Status;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import entity.User;
import model.Mail;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;


@WebServlet("/Signup")
public class Signup extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Response_DTO responsedto = new Response_DTO();
        try {
             Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        User_DTO userdto = gson.fromJson(request.getReader(), User_DTO.class);
  
      
        if(userdto.getEmail().isEmpty()){
        responsedto.setContent("Please Enter Your Email");
        } else if (Validations.isEmailValid(userdto.getEmail())){
        responsedto.setContent("Please Enter A Valid Email");
        }else if (userdto.getPassword().isEmpty()){
                responsedto.setContent("Please Enter Your Password");
        }else if(Validations.isPasswordValid(userdto.getPassword())){
                        responsedto.setContent("Please Enter A Valid Password");
        }else{
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", userdto.getEmail()));
            
            if(!criteria1.list().isEmpty()){
            responsedto.setContent("User already Exists");
            }else{
            
                int code = (int) (Math.random()* 100000);
                
                User user = new User();
                user.setEmail(userdto.getEmail());
                user.setPassword(userdto.getPassword());
                user.setVerificationCode(String.valueOf(code));
                user.setStatusStatus(1);
                user.setJoinedDate(LocalDateTime.now());
                
                Thread sendMailThread = new Thread(){
                @Override
                public void run(){
                Mail.sendMail(userdto.getEmail(), "BeFit Verification", "<h2> Your Verification Code is : "+user.getVerificationCode()+"</h2>");
                }
                };
                
                sendMailThread.start();
                session.save(user);
                session.beginTransaction().commit();
                responsedto.setSuccess(true);
                responsedto.setContent("Check Your Mail for Verification Code");
            }
            
            session.close();
        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responsedto));
          } catch (Exception e) {
  e.printStackTrace();
        }
    }   


    
}
