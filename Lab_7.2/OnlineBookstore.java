import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OnlineBookstore {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/OnlineBookstore?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";  // Update if needed
    private static final String DB_PASSWORD = "Student@2022";  // Update if needed
    private static Connection con;
    private static Scanner scanner = new Scanner(System.in);
    private static String loggedInUser = null;
    private static String favoriteGenre = null;
    private static List<String> selectedBooks = new ArrayList<>();

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
            System.out.println("✅ Connected to the Online Bookstore database!");

            while (true) {
                if (loggedInUser == null) {
                    showMainMenu();
                } else {
                    showBookstore();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showMainMenu() {
        System.out.println("\n=== 📚 Online Bookstore ===");
        System.out.println("1. Signup");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                signup();
                break;
            case 2:
                login();
                break;
            case 3:
                System.exit(0);
            default:
                System.out.println("❌ Invalid choice. Try again.");
        }
    }

    private static void signup() {
        System.out.println("\n=== ✍️ Signup ===");
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Favorite Genre (Fiction/Science/History): ");
        String genre = scanner.nextLine();

        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO users (full_name, email, password, favorite_genre) VALUES (?, ?, ?, ?)");
            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, genre);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("✅ Signup successful! Please login.");
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void login() {
        System.out.println("\n=== 🔑 Login ===");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE email=? AND password=?");
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                loggedInUser = rs.getString("full_name");
                favoriteGenre = rs.getString("favorite_genre");
                System.out.println("✅ Login successful! Welcome, " + loggedInUser + ".");
            } else {
                System.out.println("❌ Invalid credentials. Try again.");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static void showBookstore() {
        System.out.println("\n=== 📖 Welcome, " + loggedInUser + " ===");
        System.out.println("Your Favorite Genre: " + favoriteGenre);
        System.out.println("Recommended Books:");

        try {
            PreparedStatement stmt = con.prepareStatement("SELECT title FROM books WHERE genre=?");
            stmt.setString(1, favoriteGenre);
            ResultSet rs = stmt.executeQuery();

            boolean booksFound = false;
            while (rs.next()) {
                System.out.println("📚 " + rs.getString("title"));
                booksFound = true;
            }

            if (!booksFound) {
                System.out.println("⚠️ No books available for this genre.");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("❌ Error fetching books: " + e.getMessage());
        }

        System.out.println("\n1. Browse All Books");
        System.out.println("2. View Selected Books");
        System.out.println("3. Logout");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                browseBooks();
                break;
            case 2:
                viewSelectedBooks();
                break;
            case 3:
                logout();
                break;
            default:
                System.out.println("❌ Invalid choice. Returning to menu.");
        }
    }

    private static void browseBooks() {
        System.out.println("\n=== 📚 Browse Books ===");

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT title, genre FROM books");

            List<String> books = new ArrayList<>();
            while (rs.next()) {
                String bookTitle = rs.getString("title");
                books.add(bookTitle);
                System.out.println((books.size()) + ". 📖 " + bookTitle + " (Genre: " + rs.getString("genre") + ")");
            }

            rs.close();
            stmt.close();

            System.out.print("\nEnter book number to choose (0 to go back): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice > 0 && choice <= books.size()) {
                selectedBooks.add(books.get(choice - 1));
                System.out.println("✅ Added '" + books.get(choice - 1) + "' to your selected books.");
            } else if (choice == 0) {
                return;
            } else {
                System.out.println("❌ Invalid choice.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error retrieving books: " + e.getMessage());
        }
    }

    private static void viewSelectedBooks() {
        System.out.println("\n=== 📖 Your Selected Books ===");

        if (selectedBooks.isEmpty()) {
            System.out.println("⚠️ No books selected yet.");
        } else {
            for (String book : selectedBooks) {
                System.out.println("📚 " + book);
            }
        }
    }

    private static void logout() {
        loggedInUser = null;
        favoriteGenre = null;
        selectedBooks.clear();
        System.out.println("🚪 Logged out successfully!");
    }
}
