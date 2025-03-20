/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author ducdo
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.User;

public class UserDBContext extends DBContext {

    public User authenticate(String username, String password) {
        String sql = "SELECT u.*, r.RoleName, d.DepartmentName " +
                     "FROM Users u " +
                     "LEFT JOIN UserRoles ur ON u.UserID = ur.UserID " +
                     "LEFT JOIN Roles r ON ur.RoleID = r.RoleID " +
                     "JOIN Departments d ON u.DepartmentID = d.DepartmentID " +
                     "WHERE u.Username = ? AND u.Password = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            User user = null;
            List<String> roles = new ArrayList<>();
            while (rs.next()) {
                if (user == null) {
                    user = new User(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("FullName"),
                        rs.getInt("DepartmentID"),
                        rs.getInt("ManagerID") == 0 ? null : rs.getInt("ManagerID"),
                        roles,
                        rs.getString("DepartmentName")
                    );
                }
                String role = rs.getString("RoleName");
                if (role != null) roles.add(role);
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<User> getUsersByDepartment(int departmentId) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.RoleName, d.DepartmentName " +
                     "FROM Users u " +
                     "LEFT JOIN UserRoles ur ON u.UserID = ur.UserID " +
                     "LEFT JOIN Roles r ON ur.RoleID = r.RoleID " +
                     "JOIN Departments d ON u.DepartmentID = d.DepartmentID " +
                     "WHERE u.DepartmentID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, departmentId);
            ResultSet rs = stmt.executeQuery();
            User currentUser = null;
            List<String> roles = new ArrayList<>();
            while (rs.next()) {
                int userId = rs.getInt("UserID");
                if (currentUser == null || currentUser.getUserId() != userId) {
                    if (currentUser != null) users.add(currentUser);
                    roles = new ArrayList<>();
                    currentUser = new User(
                        userId,
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("FullName"),
                        rs.getInt("DepartmentID"),
                        rs.getInt("ManagerID") == 0 ? null : rs.getInt("ManagerID"),
                        roles,
                        rs.getString("DepartmentName")
                    );
                }
                String role = rs.getString("RoleName");
                if (role != null) roles.add(role);
            }
            if (currentUser != null) users.add(currentUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    // lấy toàn bộ user trong hệ thống
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.RoleName, d.DepartmentName " +
                     "FROM Users u " +
                     "LEFT JOIN UserRoles ur ON u.UserID = ur.UserID " +
                     "LEFT JOIN Roles r ON ur.RoleID = r.RoleID " +
                     "JOIN Departments d ON u.DepartmentID = d.DepartmentID";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            User currentUser = null;
            List<String> roles = new ArrayList<>();
            while (rs.next()) {
                int userId = rs.getInt("UserID");
                if (currentUser == null || currentUser.getUserId() != userId) {
                    if (currentUser != null) users.add(currentUser);
                    roles = new ArrayList<>();
                    currentUser = new User(
                        userId,
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("FullName"),
                        rs.getInt("DepartmentID"),
                        rs.getInt("ManagerID") == 0 ? null : rs.getInt("ManagerID"),
                        roles,
                        rs.getString("DepartmentName")
                    );
                }
                String role = rs.getString("RoleName");
                if (role != null) roles.add(role);
            }
            if (currentUser != null) users.add(currentUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
