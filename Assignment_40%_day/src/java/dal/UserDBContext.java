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
import java.util.logging.Logger;

public class UserDBContext {
    private static final Logger logger = Logger.getLogger(UserDBContext.class.getName());

    public User authenticate(String username, String password) {
        String sql = "SELECT u.*, r.RoleName FROM Users u " +
                     "LEFT JOIN UserRoles ur ON u.UserID = ur.UserID " +
                     "LEFT JOIN Roles r ON ur.RoleID = r.RoleID " +
                     "WHERE u.Username = ? AND u.Password = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            logger.info("Executing query with username: " + username + ", password: " + password);
            ResultSet rs = stmt.executeQuery();
            User user = null;
            List<String> roles = new ArrayList<>();
            while (rs.next()) {
                logger.info("Found user: " + rs.getString("Username"));
                if (user == null) {
                    user = new User(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getInt("DepartmentID"),
                        rs.getInt("ManagerID") == 0 ? null : rs.getInt("ManagerID"),
                        roles
                    );
                }
                String role = rs.getString("RoleName");
                if (role != null) roles.add(role);
            }
            if (user == null) logger.info("No user found for username: " + username);
            return user;
        } catch (SQLException e) {
            logger.severe("SQL Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public List<User> getUsersByDepartment(int departmentId) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, r.RoleName FROM Users u " +
                     "LEFT JOIN UserRoles ur ON u.UserID = ur.UserID " +
                     "LEFT JOIN Roles r ON ur.RoleID = r.RoleID " +
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
                        rs.getString("Email"),
                        rs.getInt("DepartmentID"),
                        rs.getInt("ManagerID") == 0 ? null : rs.getInt("ManagerID"),
                        roles
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
