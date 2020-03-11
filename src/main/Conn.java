package main;
import java.sql.*;

public class Conn {
    public static Connection connection() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mmo", "root", "root");
        return con;
    }







}


