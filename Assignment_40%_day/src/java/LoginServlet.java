import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String validUsername;
    private String validPassword;

    @Override
    public void init() throws ServletException {
        validUsername = getInitParameter("username");
        validPassword = getInitParameter("password");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Kiểm tra thông tin đăng nhập
        if (validUsername.equals(username) && validPassword.equals(password)) {
            // Đăng nhập thành công, chuyển hướng đến home.html trong thư mục view
            response.sendRedirect("view/home.html");
        } else {
            // Đăng nhập thất bại, quay lại trang login trong thư mục view
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("/view/login.html").forward(request, response);
        }
    }
}