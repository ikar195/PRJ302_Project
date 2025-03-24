/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;
import dal.LeaveRequestDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
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
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, //(kích thước tối thiểu trước khi lưu tạm lên disk)
    maxFileSize = 1024 * 1024 * 20,      //(kích thước tối đa của file)
    maxRequestSize = 1024 * 1024 * 20   //(kích thước tối đa của request)
)
public class CreateServlet extends HttpServlet {
    private final LeaveRequestDao leaveRequestDAO = new LeaveRequestDao();
    private static final long MAX_FILE_SIZE = 1024 * 1024 * 5;
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
            Part filePart;
            try {
                filePart = request.getPart("attachment");
            } catch (IllegalStateException e) {
                // Xử lý lỗi khi request vượt quá maxRequestSize
                request.setAttribute("error", "Yêu cầu quá lớn, tổng kích thước không được vượt quá 20MB!");
                request.getRequestDispatcher("/view/feature/create_request.jsp").forward(request, response);
                return;
            }

            if (filePart != null && filePart.getSize() > 0) {
                // Kiểm tra kích thước file thủ công trước khi đọc
                if (filePart.getSize() > MAX_FILE_SIZE) {
                    request.setAttribute("error", "File ảnh không được vượt quá 5MB!");
                    request.getRequestDispatcher("/view/feature/create_request.jsp").forward(request, response);
                    return;
                }
                try (InputStream inputStream = filePart.getInputStream()) {
                    attachment = inputStream.readAllBytes();
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
