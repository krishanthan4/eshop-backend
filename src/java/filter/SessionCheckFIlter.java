package filter;

import com.google.gson.Gson;
import dto.Response_DTO;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = ("/Home"))
public class SessionCheckFIlter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Gson gson = new Gson();
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpRequest.getHeader("Authorization");

        Response_DTO responsedto = new Response_DTO();
        if (httpRequest.getSession().getAttribute("user") != null) {
            chain.doFilter(request, response);
        } else {
            responsedto.setContent("NoSession");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set HTTP status code
            httpResponse.setContentType("application/json"); // Set content type
            response.getWriter().write(gson.toJson(responsedto));
        }

    }

    @Override
    public void destroy() {
    }

}
