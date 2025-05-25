import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SubmitServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int score = 0;
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement insertStmt = null;

        try {
            // Step 1: Get DB connection
            con = DBConnection.getConnection();
            if (con == null) {
                throw new SQLException("Database connection failed.");
            }

            // Step 2: Get correct answers from DB
            st = con.createStatement();
            rs = st.executeQuery("SELECT id, answer FROM questions ORDER BY id ASC LIMIT 10");

            int i = 1;
            while (rs.next()) {
                String correctAnswer = rs.getString("answer");
                String userAnswer = request.getParameter("q" + i);

                if (userAnswer != null && userAnswer.trim().equalsIgnoreCase(correctAnswer.trim())) {
                    score++;
                }
                i++;
            }

            // Step 3: Save score in session
            HttpSession session = request.getSession();
            session.setAttribute("score", score);

            // Step 4: Get user email from session
            String userEmail = (String) session.getAttribute("user");

            // Step 5: Store result in DB if user is logged in
            if (userEmail != null) {
                String insertQuery = "INSERT INTO results (user_email, score) VALUES (?, ?)";
                insertStmt = con.prepareStatement(insertQuery);
                insertStmt.setString(1, userEmail);
                insertStmt.setInt(2, score);
                insertStmt.executeUpdate();
            } else {
                log("User session expired or email is null. Score not saved.");
            }

            // Step 6: Redirect to result page
            response.sendRedirect("result.jsp");

        } catch (SQLException | NullPointerException e) {
            log("Error in SubmitServlet: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your quiz.");
        } finally {
            // Step 7: Cleanup DB resources
            try { if (rs != null) rs.close(); } catch (SQLException e) { log("Closing ResultSet failed", e); }
            try { if (st != null) st.close(); } catch (SQLException e) { log("Closing Statement failed", e); }
            try { if (insertStmt != null) insertStmt.close(); } catch (SQLException e) { log("Closing PreparedStatement failed", e); }
            try { if (con != null) con.close(); } catch (SQLException e) { log("Closing Connection failed", e); }
        }
    }
}
