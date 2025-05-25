import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/QuestionServlet")
public class QuestionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.html");
            return;
        }

        List<Map<String, String>> questions = new ArrayList<>();

        try (
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT id, question, opt1, opt2, opt3, opt4 FROM questions LIMIT 10");
        ) {

            while (rs.next()) {
                // Only add question if at least one option is non-empty
                String opt1 = rs.getString("opt1");
                String opt2 = rs.getString("opt2");
                String opt3 = rs.getString("opt3");
                String opt4 = rs.getString("opt4");

                boolean hasOptions = (opt1 != null && !opt1.trim().isEmpty()) ||
                                     (opt2 != null && !opt2.trim().isEmpty()) ||
                                     (opt3 != null && !opt3.trim().isEmpty()) ||
                                     (opt4 != null && !opt4.trim().isEmpty());

                if (hasOptions) {
                    Map<String, String> q = new HashMap<>();
                    q.put("id", String.valueOf(rs.getInt("id")));
                    q.put("question", rs.getString("question"));
                    q.put("opt1", opt1);
                    q.put("opt2", opt2);
                    q.put("opt3", opt3);
                    q.put("opt4", opt4);
                    questions.add(q);
                }
            }

            System.out.println("✅ Questions loaded: " + questions.size());

            request.setAttribute("questions", questions);
            RequestDispatcher rd = request.getRequestDispatcher("quiz.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            log("❌ Error in QuestionServlet: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to load questions.");
        }
    }
} 
