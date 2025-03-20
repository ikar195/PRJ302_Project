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
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/view/feature/create_request.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = new Date();
            String currentDateStr = sdf.format(currentDate);
            currentDate = sdf.parse(currentDateStr);
            Date startDate = sdf.parse(request.getParameter("startDate"));
            Date endDate = sdf.parse(request.getParameter("endDate"));
            if (startDate.before(currentDate)) {
                request.setAttribute("error", "Ngày bắt đầu nghỉ không thể trước ngày tạo đơn");
                request.getRequestDispatcher("/view/feature/create_request.jsp").forward(request, response);
                return;
            }
            if (endDate.before(startDate)) {
                request.setAttribute("error", "Ngày kết thúc nghỉ không thể trước ngày bắt đầu nghỉ");
                request.getRequestDispatcher("/view/feature/create_request.jsp").forward(request, response);
                return;
            }
            
            //xu lý file dinh kem
            byte[] attachment = null;
            Part filePart = request.getPart("attachment");
            if (filePart != null && filePart.getSize() > 0) {
                try (InputStream inputStream = filePart.getInputStream()) {
                    attachment = inputStream.readAllBytes(); // Đọc dữ liệu ảnh thành byte[]
                }
                // Kiểm tra kích thước (ví dụ: tối đa 5MB)
                if (attachment.length > 5 * 1024 * 1024) {
                    request.setAttribute("error", "File ảnh không được vượt quá 5MB");
                    request.getRequestDispatcher("/view/feature/create_request.jsp").forward(request, response);
                    return;
                }
            }
            
            LeaveRequest leaveRequest = new LeaveRequest();
            leaveRequest.setUserID(user.getUserId());
            leaveRequest.setStartDate(startDate);
            leaveRequest.setEndDate(endDate);
            leaveRequest.setReason(request.getParameter("reason"));
            leaveRequest.setAttachment(attachment);
            leaveRequestDAO.createLeaveRequest(leaveRequest);
            response.sendRedirect("ListRequests");
        } catch (IOException | ParseException e) {
            response.sendRedirect("feature/create_request.jsp");
        }
    }
}
