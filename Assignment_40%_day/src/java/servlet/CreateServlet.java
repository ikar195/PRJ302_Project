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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import model.LeaveRequest;
import model.User;

/**
 *
 * @author ducdo
 */
@WebServlet("/CreateRequest")
public class CreateServlet extends HttpServlet {
    private final LeaveRequestDao leaveRequestDAO = new LeaveRequestDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            LeaveRequest leaveRequest = new LeaveRequest();
            leaveRequest.setUserID(user.getUserId());
            leaveRequest.setStartDate(sdf.parse(request.getParameter("startDate")));
            leaveRequest.setEndDate(sdf.parse(request.getParameter("endDate")));
            leaveRequest.setReason(request.getParameter("reason"));
            leaveRequestDAO.createLeaveRequest(leaveRequest);
            response.sendRedirect("ListRequests");
        } catch (IOException | ParseException e) {
            response.sendRedirect("feature/create_request.jsp");
        }
    }
}
