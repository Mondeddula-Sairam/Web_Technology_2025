<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, javax.sql.*" %>
<%
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect("login.html");
        return;
    }
    String user = (String) session.getAttribute("user");
    Integer score = (Integer) session.getAttribute("score");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <style>
        body {
            font-family: Arial;
            text-align: center;
            background: #e6f0ff;
        }
        .dashboard {
            margin-top: 50px;
            background: #fff;
            padding: 30px;
            border-radius: 12px;
            display: inline-block;
            box-shadow: 0 0 10px rgba(0,0,0,0.2);
        }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            margin: 15px;
            font-size: 16px;
            color: #fff;
            background-color: #28a745;
            border: none;
            border-radius: 6px;
            text-decoration: none;
        }
        .btn.logout {
            background-color: #dc3545;
        }
        .score-box {
            margin-top: 20px;
            font-size: 18px;
            color: #333;
        }
        table {
            margin-top: 20px;
            width: 100%;
            max-width: 600px;
            border-collapse: collapse;
            margin-left: auto;
            margin-right: auto;
        }
        th, td {
            padding: 10px;
            border: 1px solid #aaa;
            background: #fff;
        }
        th {
            background-color: #d1e7ff;
        }
    </style>
</head>
<body>
    <div class="dashboard">
        <h1>Welcome, <%= user %>!</h1>
        <p>This is your dashboard.</p>

        <div class="score-box">
            <% if (score != null) { %>
                <p>Your last quiz score: <strong><%= score %> / 10</strong></p>
            <% } else { %>
                <p>You haven't taken any quiz yet.</p>
            <% } %>
        </div>

        <h3>Your Quiz History</h3>
        <table>
            <tr>
                <th>#</th>
                <th>Score</th>
                <th>Date</th>
            </tr>
            <%
                Connection con = null;
                PreparedStatement pst = null;
                ResultSet rs = null;
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/quizdb", "root", "Student@2022"); // Update credentials if needed

                    pst = con.prepareStatement("SELECT score, submitted_at FROM results WHERE user_email = ? ORDER BY submitted_at DESC");
                    pst.setString(1, user);
                    rs = pst.executeQuery();
                    int count = 1;
                    while (rs.next()) {
                        int s = rs.getInt("score");
                        Timestamp date = rs.getTimestamp("submitted_at");
            %>
            <tr>
                <td><%= count++ %></td>
                <td><%= s %> / 10</td>
                <td><%= date %></td>
            </tr>
            <%
                    }
                } catch (Exception e) {
                    out.println("<tr><td colspan='3'>Unable to load quiz history.</td></tr>");
                    e.printStackTrace();
                } finally {
                    try { if (rs != null) rs.close(); } catch (Exception e) {}
                    try { if (pst != null) pst.close(); } catch (Exception e) {}
                    try { if (con != null) con.close(); } catch (Exception e) {}
                }
            %>
        </table>

        <a href="QuestionServlet" class="btn">Start New Quiz</a>
        <a href="logout.jsp" class="btn logout">Logout</a>
    </div>
</body>
</html>
