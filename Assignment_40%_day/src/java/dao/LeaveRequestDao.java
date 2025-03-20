package dao;

import dal.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.LeaveRequest;
import dao.AgendaDAO;

public class LeaveRequestDao {

    public void createLeaveRequest(LeaveRequest request) {
        String sql = "INSERT INTO LeaveRequests (UserID, StartDate, EndDate, Reason, Status, Attachment) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, request.getUserID());
            stmt.setDate(2, new java.sql.Date(request.getStartDate().getTime()));
            stmt.setDate(3, new java.sql.Date(request.getEndDate().getTime()));
            stmt.setString(4, request.getReason());
            stmt.setString(5, "Inprogress");
            if (request.getAttachment() != null) {
                stmt.setBytes(6, request.getAttachment()); // Lưu dữ liệu ảnh
            } else {
                stmt.setNull(6, java.sql.Types.VARBINARY); // Nếu không có ảnh
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy tất cả đơn (không phân trang)
    public List<LeaveRequest> getLeaveRequestsByUser(int page, int pageSize) {
        List<LeaveRequest> requests = new ArrayList<>();
        String sql = """
                 SELECT lr.*, u.FullName, d.DepartmentName FROM LeaveRequests lr 
                 JOIN Users u ON lr.UserID = u.UserID 
                 JOIN Departments d ON u.DepartmentID = d.DepartmentID 
                 WHERE lr.UserID = u.UserID 
                 ORDER BY CASE WHEN lr.Status = 'Inprogress' THEN 0 ELSE 1 END, lr.RequestID
                 OFFSET ? ROWS FETCH NEXT ? ROWS ONLY""";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, (page - 1) * pageSize); // Offset bắt đầu từ 0
            stmt.setInt(2, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LeaveRequest request = new LeaveRequest(
                        rs.getInt("RequestID"),
                        rs.getInt("UserID"),
                        rs.getDate("StartDate"),
                        rs.getDate("EndDate"),
                        rs.getString("Reason"),
                        rs.getString("Status"),
                        rs.getTimestamp("CreatedDate")
                );
                request.setFullName(rs.getString("FullName"));
                request.setDepartmentName(rs.getString("DepartmentName"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    // Lấy đơn theo user với phân trang
    public List<LeaveRequest> getLeaveRequestsByUser(int userID, int page, int pageSize) {
        List<LeaveRequest> requests = new ArrayList<>();
        String sql = "SELECT lr.*, u.FullName, d.DepartmentName "
                + "FROM LeaveRequests lr "
                + "JOIN Users u ON lr.UserID = u.UserID "
                + "JOIN Departments d ON u.DepartmentID = d.DepartmentID "
                + "WHERE lr.UserID = ? "
                + "ORDER BY CASE WHEN lr.Status = 'Inprogress' THEN 0 ELSE 1 END, lr.RequestID "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            stmt.setInt(2, (page - 1) * pageSize); // Offset bắt đầu từ 0
            stmt.setInt(3, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LeaveRequest request = new LeaveRequest(
                        rs.getInt("RequestID"),
                        rs.getInt("UserID"),
                        rs.getDate("StartDate"),
                        rs.getDate("EndDate"),
                        rs.getString("Reason"),
                        rs.getString("Status"),
                        rs.getTimestamp("CreatedDate")
                );
                request.setFullName(rs.getString("FullName"));
                request.setDepartmentName(rs.getString("DepartmentName"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    // Lấy đơn theo phòng ban với phân trang
    public List<LeaveRequest> getLeaveRequestsByDepartment(int departmentId, int page, int pageSize) {
        List<LeaveRequest> requests = new ArrayList<>();
        String sql = "SELECT lr.*, u.FullName, d.DepartmentName "
                + "FROM LeaveRequests lr "
                + "JOIN Users u ON lr.UserID = u.UserID "
                + "JOIN Departments d ON u.DepartmentID = d.DepartmentID "
                + "WHERE u.DepartmentID = ? "
                + "ORDER BY CASE WHEN lr.Status = 'Inprogress' THEN 0 ELSE 1 END, lr.RequestID "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, departmentId);
            stmt.setInt(2, (page - 1) * pageSize);
            stmt.setInt(3, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LeaveRequest request = new LeaveRequest(
                        rs.getInt("RequestID"),
                        rs.getInt("UserID"),
                        rs.getDate("StartDate"),
                        rs.getDate("EndDate"),
                        rs.getString("Reason"),
                        rs.getString("Status"),
                        rs.getTimestamp("CreatedDate")
                );
                request.setFullName(rs.getString("FullName"));
                request.setDepartmentName(rs.getString("DepartmentName"));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    // Lấy tổng số đơn theo user
    public int getTotalLeaveRequestsByUser(int userID) {
        String sql = "SELECT COUNT(*) FROM LeaveRequests WHERE UserID = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalLeaveRequestsByUser() {
        String sql = "SELECT COUNT(*) FROM LeaveRequests";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Lấy tổng số đơn theo phòng ban
    public int getTotalLeaveRequestsByDepartment(int departmentId) {
        String sql = "SELECT COUNT(*) "
                + "FROM LeaveRequests lr "
                + "JOIN Users u ON lr.UserID = u.UserID "
                + "WHERE u.DepartmentID = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, departmentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public LeaveRequest getLeaveRequestById(int requestId) {
        String sql = "SELECT lr.*, u.FullName, d.DepartmentName, la.Comment "
                + "FROM LeaveRequests lr "
                + "JOIN Users u ON lr.UserID = u.UserID "
                + "JOIN Departments d ON u.DepartmentID = d.DepartmentID "
                + "LEFT JOIN LeaveApprovals la ON lr.RequestID = la.RequestID "
                + "WHERE lr.RequestID = ?";
        LeaveRequest request = null;
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                request = new LeaveRequest(
                        rs.getInt("RequestID"),
                        rs.getInt("UserID"),
                        rs.getDate("StartDate"),
                        rs.getDate("EndDate"),
                        rs.getString("Reason"),
                        rs.getString("Status"),
                        rs.getTimestamp("CreatedDate")
                );
                request.setFullName(rs.getString("FullName"));
                request.setDepartmentName(rs.getString("DepartmentName"));
                request.setComment(rs.getString("Comment"));
                request.setAttachment(rs.getBytes("Attachment")); // Lấy dữ liệu ảnh
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return request;
    }

    public void updateLeaveRequestStatus(int requestId, String status, int approverId, String comment) {
        String updateLeaveRequestSql = "UPDATE LeaveRequests SET Status = ? WHERE RequestID = ?";
        String insertApprovalSql = "INSERT INTO LeaveApprovals (RequestID, ApproverID, ApprovalStatus, Comment) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            try (PreparedStatement updateLeaveStmt = conn.prepareStatement(updateLeaveRequestSql); PreparedStatement insertApprovalStmt = conn.prepareStatement(insertApprovalSql)) {

                // Cập nhật trạng thái đơn xin nghỉ
                updateLeaveStmt.setString(1, status);
                updateLeaveStmt.setInt(2, requestId);
                updateLeaveStmt.executeUpdate();

                // Ghi nhận phê duyệt
                insertApprovalStmt.setInt(1, requestId);
                insertApprovalStmt.setInt(2, approverId);
                insertApprovalStmt.setString(3, status);
                insertApprovalStmt.setString(4, comment);
                insertApprovalStmt.executeUpdate();

                // Nếu phê duyệt, cập nhật agenda
                if ("Approved".equalsIgnoreCase(status)) {
                    AgendaDAO.updateAgendaForLeaveRequest(conn, requestId);
                }

                conn.commit(); // Commit transaction nếu tất cả thành công
            } catch (SQLException e) {
                conn.rollback(); // Rollback nếu có lỗi
                throw new RuntimeException("Error processing leave request approval", e);
            } finally {
                conn.setAutoCommit(true); // Trả lại chế độ tự động commit
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

}
