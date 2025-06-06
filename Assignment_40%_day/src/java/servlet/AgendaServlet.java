package servlet;

import dal.UserDBContext;
import dal.AgendaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import model.User;

@WebServlet("/Agenda")
public class AgendaServlet extends HttpServlet {

    private final AgendaDAO agendaDAO = new AgendaDAO();
    private final UserDBContext userDAO = new UserDBContext();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || (!user.getRoles().contains("Manager") && !user.getDepartmentName().contains("Manager"))) {
            response.sendRedirect("login");
            return;
        }
        request.getRequestDispatcher("/view/feature/agenda.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || (!user.getRoles().contains("Manager") && !user.getDepartmentName().contains("Manager"))) {
            response.sendRedirect("login");
            return;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date startDate = sdf.parse(request.getParameter("startDate"));
            java.util.Date endDate = sdf.parse(request.getParameter("endDate"));

            if (endDate.before(startDate)) {
                request.setAttribute("error", "Ngày kết thúc không thể trước ngày bắt đầu");
                request.getRequestDispatcher("/view/feature/agenda.jsp").forward(request, response);
                return;
            }
            // Kiểm tra xem user thuộc department "Manager" hay không
            boolean isManagerDepartment = user.getDepartmentName() != null && user.getDepartmentName().contains("Manager");

            if (isManagerDepartment) {
                // Nếu là Manager, lấy toàn bộ agenda và user
                request.setAttribute("agenda", agendaDAO.getAllAgendas(startDate, endDate));
                request.setAttribute("users", userDAO.getAllUsers());
            } else {
                // Nếu không, chỉ lấy theo department của user
                request.setAttribute("agenda", agendaDAO.getAgenda(user.getDepartmentId(), startDate, endDate));
                request.setAttribute("users", userDAO.getUsersByDepartment(user.getDepartmentId()));
            }

            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.getRequestDispatcher("/view/feature/agenda.jsp").forward(request, response);
        } catch (ServletException | IOException | ParseException e) {
            request.getRequestDispatcher("/view/feature/agenda.jsp").forward(request, response);
        }

    }
}
