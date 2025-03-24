package servlet;

import dal.LeaveRequestDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.User;

@WebServlet("/ListRequests")
public class ListRequestsServlet extends HttpServlet {
    private final LeaveRequestDao leaveRequestDAO = new LeaveRequestDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        // Lấy tham số page từ request, mặc định là 1
        int page = 1;
        int pageSize = 7; // Số bản ghi mỗi trang
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            page = Integer.parseInt(pageParam);
        }

        // Lấy danh sách đơn và tổng số bản ghi
        List requests;
        int totalRecords;
        if (user.getRoles().contains("Manager")) {
            requests = leaveRequestDAO.getLeaveRequestsByDepartment(user.getDepartmentId(), page, pageSize);
            totalRecords = leaveRequestDAO.getTotalLeaveRequestsByDepartment(user.getDepartmentId());
        } 
        else{
            requests = leaveRequestDAO.getLeaveRequestsByUser(user.getUserId(), page, pageSize);
            totalRecords = leaveRequestDAO.getTotalLeaveRequestsByUser(user.getUserId());
        }
        if (user.getDepartmentName().contains("Manager")){
            requests = leaveRequestDAO.getLeaveRequestsByUser(page, pageSize);
            totalRecords = leaveRequestDAO.getTotalLeaveRequestsByUser();
        }

        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // Truyền dữ liệu tới JSP
        request.setAttribute("requests", requests);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);

        request.getRequestDispatcher("/view/feature/list_requests.jsp").forward(request, response);
    }
}