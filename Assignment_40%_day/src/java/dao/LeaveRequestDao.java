package dao;

import dal.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.LeaveRequest;
import java.util.logging.Logger;

public class LeaveRequestDao {
    public void createLeaveRequest(LeaveRequest request) {
        String sql = "INSERT INTO LeaveRequests (UserID, StartDate, EndDate, Reason, Status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, request.getUserID());
            stmt.setDate(2, new java.sql.Date(request.getStartDate().getTime()));
            stmt.setDate(3, new java.sql.Date(request.getEndDate().getTime()));
            stmt.setString(4, request.getReason());
            stmt.setString(5, "Inprogress");
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
                                          ORDER BY lr.RequestID
                                          OFFSET ? ROWS FETCH NEXT ? ROWS ONLY""";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        String sql = "SELECT lr.*, u.FullName, d.DepartmentName " +
                     "FROM LeaveRequests lr " +
                     "JOIN Users u ON lr.UserID = u.UserID " +
                     "JOIN Departments d ON u.DepartmentID = d.DepartmentID " +
                     "WHERE lr.UserID = ? " +
                     "ORDER BY lr.RequestID " +
                     "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        String sql = "SELECT lr.*, u.FullName, d.DepartmentName " +
                     "FROM LeaveRequests lr " +
                     "JOIN Users u ON lr.UserID = u.UserID " +
                     "JOIN Departments d ON u.DepartmentID = d.DepartmentID " +
                     "WHERE u.DepartmentID = ? " +
                     "ORDER BY lr.RequestID " +
                     "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        String sql = "SELECT COUNT(*) " +
                     "FROM LeaveRequests lr " +
                     "JOIN Users u ON lr.UserID = u.UserID " +
                     "WHERE u.DepartmentID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        String sql = "SELECT lr.*, u.FullName, d.DepartmentName " +
                     "FROM LeaveRequests lr " +
                     "JOIN Users u ON lr.UserID = u.UserID " +
                     "JOIN Departments d ON u.DepartmentID = d.DepartmentID " +
                     "WHERE lr.RequestID = ?";
        LeaveRequest request = null;
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
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
                return request;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
    throw new RuntimeException(e);
        }
        return null;
    }
    
    public void updateLeaveRequestStatus(int requestId, String status, int approverId) {
        String sql = "UPDATE LeaveRequests SET Status = ? WHERE RequestID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, requestId);
            stmt.executeUpdate();

            String approvalSql = "INSERT INTO LeaveApprovals (RequestID, ApproverID, ApprovalStatus) VALUES (?, ?, ?)";
            try (PreparedStatement approvalStmt = conn.prepareStatement(approvalSql)) {
                approvalStmt.setInt(1, requestId);
                approvalStmt.setInt(2, approverId);
                approvalStmt.setString(3, status);
                approvalStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}