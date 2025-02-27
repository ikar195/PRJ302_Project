import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {

        // Lấy thông tin người dùng nhập
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Lấy thông tin cấu hình từ web.xml
        ServletContext context = getServletContext();
        String configUsername = context.getInitParameter("username");
        String configPassword = context.getInitParameter("password");

        // So sánh thông tin đăng nhập
        if(configUsername != null && configPassword != null &&
           configUsername.equals(username) && configPassword.equals(password)){
            // Đăng nhập thành công: chuyển hướng đến trang home (ví dụ: view/home.html)
            response.sendRedirect(request.getContextPath() + "/view/home.html");
        } else {
            // Đăng nhập thất bại: có thể chuyển về trang login với thông báo lỗi hoặc hiển thị lỗi đơn giản
            response.getWriter().println("Login failed. Please try again.");
        }
    }
}
