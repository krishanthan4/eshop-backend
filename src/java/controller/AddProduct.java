
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Category;
import entity.Color;
import entity.Condition;
import entity.Model;
import entity.Product;
import entity.ProductImg;
import entity.Status;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.ApplicationPath;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import util.config;


@MultipartConfig
@WebServlet("/AddProduct")
public class AddProduct extends HttpServlet {
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Response_DTO response_DTO = new Response_DTO();
    Gson gson = new Gson();

    // Extract form data
    String modelId = request.getParameter("modelId");
    String title = request.getParameter("title");
    String description = request.getParameter("description");
    String colorId = request.getParameter("colorId");
    String conditionId = request.getParameter("conditionId");
    String price = request.getParameter("price");
    String quantity = request.getParameter("quantity");

    // Extract images from the form
    Part image1 = request.getPart("image1");
    Part image2 = request.getPart("image2");
    Part image3 = request.getPart("image3");

    // Open Hibernate session
    Session session = HibernateUtil.getSessionFactory().openSession();

    try {
        session.beginTransaction();

        // Validate input data
        if (!Validations.isInteger(modelId)) {
            response_DTO.setContent("Invalid Model");
        } else if (!Validations.isInteger(colorId)) {
            response_DTO.setContent("Invalid Color");
        } else if (!Validations.isInteger(conditionId)) {
            response_DTO.setContent("Invalid Condition");
        } else if (title == null || title.isEmpty()) {
            response_DTO.setContent("Please fill Title");
        } else if (description == null || description.isEmpty()) {
            response_DTO.setContent("Please fill Description");
        } else if (price == null || price.isEmpty()) {
            response_DTO.setContent("Please fill Price");
        } else if (!Validations.isDouble(price)) {
            response_DTO.setContent("Invalid Price");
        } else if (Double.parseDouble(price) <= 0) {
            response_DTO.setContent("Price must be greater than 0");
        } else if (quantity == null || quantity.isEmpty()) {
            response_DTO.setContent("Please fill Quantity");
        } else if (!Validations.isInteger(quantity)) {
            response_DTO.setContent("Invalid Quantity");
        } else if (Integer.parseInt(quantity) <= 0) {
            response_DTO.setContent("Quantity must be greater than 0");
        } else if (image1.getSubmittedFileName() == null || image1.getSubmittedFileName().isEmpty()) {
            response_DTO.setContent("Please upload Image 1");
        } else if (image2.getSubmittedFileName() == null || image2.getSubmittedFileName().isEmpty()) {
            response_DTO.setContent("Please upload Image 2");
        } else if (image3.getSubmittedFileName() == null || image3.getSubmittedFileName().isEmpty()) {
            response_DTO.setContent("Please upload Image 3");
        } else {
            // Fetch model, color, and condition from the database
            Model model = (Model) session.get(Model.class, Integer.valueOf(modelId));
            Color color = (Color) session.get(Color.class, Integer.valueOf(colorId));
            Condition condition = (Condition) session.get(Condition.class, Integer.valueOf(conditionId));

            if (model == null) {
                response_DTO.setContent("Please Select a Valid Model");
            } else if (color == null) {
                response_DTO.setContent("Please Select a Valid Color");
            } else if (condition == null) {
                response_DTO.setContent("Please Select a Valid Condition");
            } else {
                // Create new Product object and set attributes
                Product product = new Product();
                product.setModel(model);
                product.setColor(color);
                product.setCondition(condition);
                product.setTitle(title);
                product.setDescription(description);
                product.setPrice(Double.valueOf(price));
                product.setQty(Integer.valueOf(quantity));
                product.setDatetimeAdded(new Date());

                // Set product status to Active (assuming status ID 1 is active)
                Status product_Status = (Status) session.load(Status.class, 1);
                product.setStatus(product_Status);

                // Get the current user from session
                String userEmail = (String) request.getSession().getAttribute("user");
                User user = (User) session.createCriteria(User.class)
                    .add(Restrictions.eq("email", userEmail))
                    .uniqueResult();
                product.setUserEmail(user);

                // Save product to the database
                session.save(product);

                // Save images to the product's folder
                String applicationPath = config.CLIENT_LOCATION+"/public/images";
                File folder = new File(applicationPath + "/product_images/" + product.getId());
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                saveImage(image1, folder, product.getId() + "image1.png");
                saveImage(image2, folder, product.getId() + "image2.png");
                saveImage(image3, folder, product.getId() + "image3.png");

                // Save images info to the database
                saveProductImage(session, product, "product_images/" +product.getId()+"/"+ product.getId() + "image1.png");
                saveProductImage(session, product, "product_images/"+product.getId()+"/" + product.getId() + "image2.png");
                saveProductImage(session, product, "product_images/"+product.getId()+"/" + product.getId() + "image3.png");

                session.getTransaction().commit();

                // Set success response
                response_DTO.setSuccess(true);
                response_DTO.setContent("New Product Added Successfully");
            }
        }
    } catch (IOException | NumberFormatException | HibernateException e) {
        if (session.getTransaction() != null) {
            session.getTransaction().rollback();
        }
        response_DTO.setContent("An error occurred: " + e.getMessage());
        e.printStackTrace(); // Log the exception
    } finally {
        session.close();
    }

    // Send response
    response.setContentType("application/json");
    response.getWriter().write(gson.toJson(response_DTO));
}

// Helper method to save images
private void saveImage(Part imagePart, File folder, String fileName) throws IOException {
    File file = new File(folder, fileName);
    try (InputStream inputStream = imagePart.getInputStream()) {
        Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}

// Helper method to save product images info to the database
private void saveProductImage(Session session, Product product, String imgPath) {
    ProductImg productImg = new ProductImg();
    productImg.setImgPath(imgPath);
    productImg.setProduct(product);
    session.save(productImg);
}



}
