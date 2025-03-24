/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dal.LeaveRequestDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import model.LeaveRequest;

/**
 *
 * @author ducdo
 */
@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {
    private final LeaveRequestDao leaveRequestDAO = new LeaveRequestDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int requestId = Integer.parseInt(request.getParameter("requestId"));
        LeaveRequest leaveRequest = leaveRequestDAO.getLeaveRequestById(requestId);

        if (leaveRequest != null && leaveRequest.getAttachment() != null) {
            response.setContentType("image/jpg");
            response.setContentLength(leaveRequest.getAttachment().length);
            try (OutputStream out = response.getOutputStream()) {
                out.write(leaveRequest.getAttachment());
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy ảnh");
        }
    }
}
