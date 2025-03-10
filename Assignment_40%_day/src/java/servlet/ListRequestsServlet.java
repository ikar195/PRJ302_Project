/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.LeaveRequestDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.User;

/**
 *
 * @author ducdo
 */
@WebServlet("/ListRequests")
public class ListRequestsServlet extends HttpServlet {
    private final LeaveRequestDao leaveRequestDAO = new LeaveRequestDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        if (user.getRoles().contains("Manager")) {
            request.setAttribute("requests", leaveRequestDAO.getLeaveRequestsByDepartment(user.getDepartmentId()));
        } 
        else{
            request.setAttribute("requests", leaveRequestDAO.getLeaveRequestsByUser(user.getUserId()));
        }
        if (user.getDepartmentName().contains("Manager")){
            request.setAttribute("requests", leaveRequestDAO.getLeaveRequestsByUser());
        }
        
        request.getRequestDispatcher("/view/feature/list_requests.jsp").forward(request, response);
    }
}
