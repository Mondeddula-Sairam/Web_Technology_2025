import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Basic validation
        if (name == null || email == null || password == null ||
            name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            response.getWriter().println("All fields are required.");
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "INSERT INTO users(name, email, password) VALUES (?, ?, ?)")) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                response.sendRedirect("login.html");
            } else {
                response.getWriter().println("Registration failed. Please try again.");
            }

        } catch (SQLException e) {
            log("Database error in RegisterServlet: ", e); // Replaces printStackTrace
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error during registration.");
        } catch (Exception e) {
            log("Unexpected error in RegisterServlet: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected server error.");
        }
    }
}
