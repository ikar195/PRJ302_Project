package servlet;

import dal.UserDBContext;
import dao.AgendaDAO;
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
        if (user == null || (!user.getRoles().contains("Manager") && !user.getRoles().contains("Admin"))) {
            response.sendRedirect("login.jsp");
            return;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date startDate = sdf.parse(request.getParameter("startDate"));
            java.util.Date endDate = sdf.parse(request.getParameter("endDate"));
            request.setAttribute("agenda", agendaDAO.getAgenda(user.getDepartmentId(), startDate, endDate));
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.setAttribute("users", userDAO.getUsersByDepartment(user.getDepartmentId()));
            request.getRequestDispatcher("/view/feature/agenda.jsp").forward(request, response);
        } catch (ServletException | IOException | ParseException e) {
            request.getRequestDispatcher("/view/feature/agenda.jsp").forward(request, response);
        }
    }
}
