/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package dal;

import java.sql.*;

public class DBContext {
    private static final String URL = "jdbc:sqlserver://localhost\\MSSQLSERVER:1433;databaseName=Assignment;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "ducdm"; 
    private static final String PASSWORD = "12345678"; 

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
