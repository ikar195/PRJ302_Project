package dao;

import dal.DBContext;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

public class AgendaDAO {
    public Map<Integer, Map<Date, String>> getAgenda(int departmentId, Date startDate, Date endDate) {
        Map<Integer, Map<Date, String>> agenda = new HashMap<>();
        String sql = "SELECT a.UserID, a.Date, a.Status FROM Agenda a " +
                     "JOIN Users u ON a.UserID = u.UserID " +
                     "WHERE u.DepartmentID = ? AND a.Date BETWEEN ? AND ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
}