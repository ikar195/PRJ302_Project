package servlet;

import dao.LeaveRequestDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.LeaveRequest;
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
        int pageSize = 8; // Số bản ghi mỗi trang
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            page = Integer.parseInt(pageParam);
        }

        // Lấy tổng số bản ghi
        int totalRecords;
        if (user.getRoles().contains("Manager")) {
            totalRecords = leaveRequestDAO.getTotalLeaveRequestsByDepartment(user.getDepartmentId());
        } else {
            totalRecords = leaveRequestDAO.getTotalLeaveRequestsByUser(user.getUserId());
        }
        if (user.getDepartmentName().contains("Manager")) {
            totalRecords = leaveRequestDAO.getTotalLeaveRequestsByUser();
        }

        // Tính tổng số trang tạm thời để lấy toàn bộ dữ liệu
        int totalPagesTemp = (int) Math.ceil((double) totalRecords / pageSize);

        // Gộp toàn bộ danh sách từ các trang
        List<LeaveRequest> allRequests = new ArrayList<>();
        if (user.getRoles().contains("Manager")) {
            for (int i = 1; i <= totalPagesTemp; i++) {
                allRequests.addAll(leaveRequestDAO.getLeaveRequestsByDepartment(user.getDepartmentId(), i, pageSize));
            }
        } else {
            for (int i = 1; i <= totalPagesTemp; i++) {
                allRequests.addAll(leaveRequestDAO.getLeaveRequestsByUser(user.getUserId(), i, pageSize));
            }
        }
        if (user.getDepartmentName().contains("Manager")) {
            for (int i = 1; i <= totalPagesTemp; i++) {
                allRequests.addAll(leaveRequestDAO.getLeaveRequestsByUser(i, pageSize));
            }
        }

        // Sắp xếp toàn bộ danh sách: "Inprogress" lên đầu
        Collections.sort(allRequests, (LeaveRequest r1, LeaveRequest r2) -> {
            if ("Inprogress".equals(r1.getStatus())) {
                return -1;
            } else if ("Inprogress".equals(r2.getStatus())) {
                return 1;
            }
            return 0; // Giữ nguyên thứ tự nếu không có "Inprogress"
        });

        // Phân trang thủ công
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalRecords);
        List<LeaveRequest> paginatedRequests = allRequests.subList(startIndex, endIndex);

        // Truyền dữ liệu tới JSP
        request.setAttribute("requests", paginatedRequests);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);

        request.getRequestDispatcher("/view/feature/list_requests.jsp").forward(request, response);
    }
}