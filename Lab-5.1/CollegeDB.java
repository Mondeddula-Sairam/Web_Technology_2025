import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class CollegeDB {
    private static final String URL = "jdbc:mysql://localhost:3306/collegeDB"; // Change database name
    private static final String USER = "root"; // Change if needed
    private static final String PASSWORD = "Student@2022"; // Add your password if set
    private static final String TABLE_NAME = "Courses";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            createTable(conn);
            Scanner scanner = new Scanner(System.in);
            
            while (true) {
                System.out.println("Select Operation: 1-Insert 2-Update 3-Delete 4-Display 5-Exit");
                int choice = scanner.nextInt();
                
                switch (choice) {
                    case 1:
                        System.out.println("Enter CourseID, Name, and Credits:");
                        int id = scanner.nextInt();
                        scanner.nextLine(); 
                        String name = scanner.nextLine();
                        int credits = scanner.nextInt();
                        insertCourse(conn, id, name, credits);
                        break;
                    case 2:
                        System.out.println("Enter CourseID to update and new Credits:");
                        int updateId = scanner.nextInt();
                        int newCredits = scanner.nextInt();
                        updateCourse(conn, updateId, newCredits);
                        break;
                    case 3:
                        System.out.println("Enter CourseID to delete:");
                        int deleteId = scanner.nextInt();
                        deleteCourse(conn, deleteId);
                        break;
                    case 4:
                        displayCourses(conn);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                     " (CourseID INT PRIMARY KEY, Name VARCHAR(255), Credits INT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Table 'Courses' created successfully.");
        }
    }

    private static void insertCourse(Connection conn, int id, String name, int credits) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, credits);
            pstmt.executeUpdate();
            System.out.println("Record inserted successfully.");
        }
    }

    private static void updateCourse(Connection conn, int id, int credits) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET Credits = ? WHERE CourseID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, credits);
            pstmt.setInt(2, id);
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println(rowsUpdated > 0 ? "Record updated successfully." : "CourseID not found.");
        }
    }

    private static void deleteCourse(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE CourseID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println(rowsDeleted > 0 ? "Record deleted successfully." : "CourseID not found.");
        }
    }

    private static void displayCourses(Connection conn) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("CourseID | Name | Credits");
            while (rs.next()) {
                System.out.println(rs.getInt("CourseID") + " | " + rs.getString("Name") + " | " + rs.getInt("Credits"));
            }
        }
    }
}
