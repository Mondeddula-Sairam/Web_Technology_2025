
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/quizdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Student@2022";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("Error: MySQL JDBC Driver not found. " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error: Could not connect to the database. " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("✅ Connected to database successfully.");
        } else {
            System.out.println("❌ Failed to connect to database.");
        }
    }
}
