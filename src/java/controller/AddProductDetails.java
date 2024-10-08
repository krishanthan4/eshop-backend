/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Category;
import entity.Color;
import entity.Model;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;


@WebServlet("/AddProductDetails")
public class AddProductDetails extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();

        Session session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria1 = session.createCriteria(Category.class);
        criteria1.addOrder(Order.asc("catName"));
        List<Category> categoryList = criteria1.list();

        Criteria criteria2 = session.createCriteria(Model.class);
        criteria2.addOrder(Order.asc("modelName"));
        List<Model> modelList = criteria2.list();

        Criteria criteria3 = session.createCriteria(Color.class);
        criteria3.addOrder(Order.asc("clrName"));
        List<Color> colorList = criteria3.list();

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("categoryList", gson.toJsonTree(categoryList));
        jsonObject.add("modelList", gson.toJsonTree(modelList));
        jsonObject.add("colorList", gson.toJsonTree(colorList));

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));
        session.close();

    }

}
