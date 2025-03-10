/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author ducdo
 */
import dal.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.LeaveRequest;

public class LeaveRequestDao{
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
        }
    }

    public List<LeaveRequest> getLeaveRequestsByUser() {
        List<LeaveRequest> requests = new ArrayList<>();
        String sql = "SELECT lr.*, u.FullName, d.DepartmentName " +
                     "FROM LeaveRequests lr " +
                     "JOIN Users u ON lr.UserID = u.UserID " +
                     "JOIN Departments d ON u.DepartmentID = d.DepartmentID ";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        }
        return requests;
    }
    public List<LeaveRequest> getLeaveRequestsByUser(int userID) {
        List<LeaveRequest> requests = new ArrayList<>();
        String sql = "SELECT lr.*, u.FullName, d.DepartmentName " +
                     "FROM LeaveRequests lr " +
                     "JOIN Users u ON lr.UserID = u.UserID " +
                     "JOIN Departments d ON u.DepartmentID = d.DepartmentID " +
                "Where lr.UserID = ?"
                ;
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
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
        }
        return requests;
    }

    public List<LeaveRequest> getLeaveRequestsByDepartment(int departmentId) {
        List<LeaveRequest> requests = new ArrayList<>();
        String sql = "SELECT lr.*, u.FullName, d.DepartmentName " +
                     "FROM LeaveRequests lr " +
                     "JOIN Users u ON lr.UserID = u.UserID " +
                     "JOIN Departments d ON u.DepartmentID = d.DepartmentID " +
                     "WHERE u.DepartmentID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, departmentId);
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
        }
        return requests;
    }

    public LeaveRequest getLeaveRequestById(int requestId) {
        String sql = "SELECT lr.*, u.FullName, d.DepartmentName " +
                     "FROM LeaveRequests lr " +
                     "JOIN Users u ON lr.UserID = u.UserID " +
                     "JOIN Departments d ON u.DepartmentID = d.DepartmentID " +
                     "WHERE lr.RequestID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
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
                return request;
            }
        } catch (SQLException e) {
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
        }
    }
}
