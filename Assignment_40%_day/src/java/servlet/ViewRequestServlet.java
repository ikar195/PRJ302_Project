package servlet;

import dao.LeaveRequestDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.LeaveRequest;
import model.User;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/ViewRequest")
public class ViewRequestServlet extends HttpServlet {

    private final LeaveRequestDao leaveRequestDAO = new LeaveRequestDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String requestIdParam = request.getParameter("id");
        if (requestIdParam == null || requestIdParam.isEmpty()) {
            response.sendRedirect("ListRequests");
            return;
        }

        int requestId;
        try {
            requestId = Integer.parseInt(requestIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect("ListRequests");
            return;
        }

        LeaveRequest leaveRequest = leaveRequestDAO.getLeaveRequestById(requestId);
        System.out.println("LeaveRequest: " + leaveRequest);
        if (leaveRequest == null) {
            request.setAttribute("error", "Không tìm thấy đơn xin nghỉ với ID: " + requestId);
            request.getRequestDispatcher("/view/feature/view_request.jsp").forward(request, response);
            return;
        }
        if (user.getUserId() != leaveRequest.getUserID() && !user.getRoles().contains("Manager")) {
            request.setAttribute("error", "Bạn không có quyền xem đơn này.");
            request.getRequestDispatcher("/view/feature/view_request.jsp").forward(request, response);
            return;
        }
        request.setAttribute("request", leaveRequest);
        request.setAttribute("currentPage", request.getParameter("page"));
        request.getRequestDispatcher("/view/feature/view_request.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("ListRequests");
            return;
        }

        String requestIdParam = request.getParameter("requestId");
        String action = request.getParameter("action");
        String comment = request.getParameter("comment");
        String currentPage = request.getParameter("page");

        if (requestIdParam == null || action == null) {
            response.sendRedirect("ListRequests");
            return;
        }

        int requestId;
        try {
            requestId = Integer.parseInt(requestIdParam);
        } catch (NumberFormatException e) {
            response.sendRedirect("ListRequests");
            return;
        }

        LeaveRequest leaveRequest = leaveRequestDAO.getLeaveRequestById(requestId); //lay thong tin don xin nghi
        if (leaveRequest == null) {
            response.sendRedirect("ListRequests");
            return;
        }

        boolean isCreator = user.getUserId() == leaveRequest.getUserID(); //ktra quyen
        boolean isManagerRole = user.getRoles().contains("Manager");
        boolean isManagerDept = user.getDepartmentName().contains("Manager");

        if (!isManagerDept && ((isCreator && isManagerRole) || (!isManagerRole && !isManagerDept))) {
            request.setAttribute("error", "Bạn không có quyền phê duyệt đơn này.");
            request.setAttribute("request", leaveRequest);
            request.getRequestDispatcher("/view/feature/view_request.jsp").forward(request, response);
            return;
        }

        String status = "Approved".equals(action) ? "Approved" : "Rejected";
        leaveRequestDAO.updateLeaveRequestStatus(requestId, status, user.getUserId(), comment);

        response.sendRedirect("ListRequests?page=" + (currentPage != null ? currentPage : "1"));
    }
}
