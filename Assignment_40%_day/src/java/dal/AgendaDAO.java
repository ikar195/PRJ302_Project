package dal;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class AgendaDAO {

    //by Department
    public Map<Integer, Map<Date, String>> getAgenda(int departmentId, Date startDate, Date endDate) {
        Map<Integer, Map<Date, String>> agenda = new HashMap<>();
        String sql = "SELECT a.UserID, a.Date, a.Status FROM Agenda a "
                + "JOIN Users u ON a.UserID = u.UserID "
                + "WHERE u.DepartmentID = ? AND a.Date BETWEEN ? AND ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, departmentId);
            stmt.setDate(2, new java.sql.Date(startDate.getTime()));
            stmt.setDate(3, new java.sql.Date(endDate.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("UserID");
                Date date = rs.getDate("Date");
                String status = rs.getString("Status");
                agenda.computeIfAbsent(userId, k -> new HashMap<>()).put(date, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agenda;
    }

    //all
    public Map<Integer, Map<Date, String>> getAllAgendas(Date startDate, Date endDate) {
        Map<Integer, Map<Date, String>> agenda = new HashMap<>();
        String sql = "SELECT a.UserID, a.Date, a.Status FROM Agenda a "
                + "WHERE a.Date BETWEEN ? AND ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(startDate.getTime()));
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("UserID");
                Date date = rs.getDate("Date");
                String status = rs.getString("Status");
                agenda.computeIfAbsent(userId, k -> new HashMap<>()).put(date, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agenda;
    }

    public static void updateAgendaForLeaveRequest(Connection conn, int requestId) throws SQLException {
    String getLeaveDatesSql = "SELECT UserID, StartDate, EndDate FROM LeaveRequests WHERE RequestID = ?";
    
    try (PreparedStatement getLeaveStmt = conn.prepareStatement(getLeaveDatesSql)) {
        getLeaveStmt.setInt(1, requestId);
        ResultSet rs = getLeaveStmt.executeQuery();

        if (rs.next()) {
            int userId = rs.getInt("UserID");
            Date startDate = rs.getDate("StartDate");
            Date endDate = rs.getDate("EndDate");

            long oneDay = 24 * 60 * 60 * 1000; // Một ngày tính bằng mili-giây

            // Dùng batch update với MERGE để tránh lỗi ON DUPLICATE KEY UPDATE
            try (Statement stmt = conn.createStatement()) {
                for (long time = startDate.getTime(); time <= endDate.getTime(); time += oneDay) {
                    String sql = "MERGE INTO Agenda AS target " +
                                 "USING (SELECT " + userId + " AS UserID, '" + new java.sql.Date(time) + "' AS Date) AS source " +
                                 "ON target.UserID = source.UserID AND target.Date = source.Date " +
                                 "WHEN MATCHED THEN UPDATE SET target.Status = 'OnLeave' " +
                                 "WHEN NOT MATCHED THEN INSERT (UserID, Date, Status) VALUES (source.UserID, source.Date, 'OnLeave');";
                    
                    stmt.addBatch(sql);
                }
                stmt.executeBatch(); // Thực thi batch update
            }
        }
    }
}


}
