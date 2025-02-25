import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MySQLCRUD {
    // Database credentials
    static final String DB_URL = "jdbc:mysql://localhost:3306/MySQLCRUD"; // Change if needed
    static final String USER = "root"; // Replace with your MySQL username
    static final String PASSWORD = "Student@2022"; // Replace with your MySQL password

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            createTable(conn);
            while (true) {
                System.out.println("\nSelect Operation:");
                System.out.println("1. Insert Course\n2. Update Course\n3. Delete Course\n4. Display Courses\n5. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> insertCourse(conn, scanner);
                    case 2 -> updateCourse(conn, scanner);
                    case 3 -> deleteCourse(conn, scanner);
                    case 4 -> displayCourses(conn);
                    case 5 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice! Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create table if not exists
    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Courses (" +
                     "CourseID INT PRIMARY KEY, " +
                     "Name VARCHAR(100), " +
                     "Credits INT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Table 'Courses' is ready.");
        }
    }

    // Insert a course
    private static void insertCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Course ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Course Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Credits: ");
        int credits = scanner.nextInt();

        String sql = "INSERT INTO Courses (CourseID, Name, Credits) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, credits);
            pstmt.executeUpdate();
            System.out.println("Record inserted successfully.");
        }
    }

    // Update a course
    private static void updateCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Course ID to update: ");
        int id = scanner.nextInt();

        System.out.print("Enter new Credits: ");
        int credits = scanner.nextInt();

        String sql = "UPDATE Courses SET Credits = ? WHERE CourseID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, credits);
            pstmt.setInt(2, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Record updated successfully.");
            } else {
                System.out.println("Course ID not found.");
            }
        }
    }

    // Delete a course
    private static void deleteCourse(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Course ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM Courses WHERE CourseID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Record deleted successfully.");
            } else {
                System.out.println("Course ID not found.");
            }
        }
    }

    // Display all courses
    private static void displayCourses(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Courses";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nCourses Table:");
            System.out.println("--------------------------------------");
            while (rs.next()) {
                System.out.println("CourseID: " + rs.getInt("CourseID") + 
                                   " | Name: " + rs.getString("Name") + 
                                   " | Credits: " + rs.getInt("Credits"));
            }
            System.out.println("--------------------------------------");
        }
    }
}