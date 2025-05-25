<%@ page import="java.util.*, java.util.Map" %>
<%@ page session="true" %>
<%
    // Redirect to login page if user is not logged in
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.html");
        return;
    }

    // Get questions safely
    List<Map<String, String>> questions = (List<Map<String, String>>) request.getAttribute("questions");
    if (questions == null) {
        questions = new ArrayList<>();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Online Quiz</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #eef5f9;
            padding: 20px;
        }
        .quiz-container {
            max-width: 700px;
            margin: auto;
            background-color: white;
            padding: 30px 40px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.15);
        }
        h2 {
            text-align: center;
            margin-bottom: 25px;
        }
        .question {
            margin-bottom: 25px;
        }
        .question p {
            font-weight: bold;
            margin-bottom: 10px;
        }
        label {
            display: block;
            margin-bottom: 5px;
        }
        input[type="submit"] {
            margin-top: 20px;
            padding: 10px 20px;
            font-size: 16px;
            background-color: #4caf50;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <div class="quiz-container">
        <h2>Welcome, <%= session.getAttribute("user") %>! Take Your Quiz</h2>
        <form action="SubmitServlet" method="post">
            <%
                int index = 1;
                if (!questions.isEmpty()) {
                    for (Map<String, String> q : questions) {
                        String opt1 = q.get("opt1");
                        String opt2 = q.get("opt2");
                        String opt3 = q.get("opt3");
                        String opt4 = q.get("opt4");
            %>
                        <div class="question">
                            <p><%= index + ". " + q.get("question") %></p>

                            <% if (opt1 != null && !opt1.trim().isEmpty()) { %>
                                <label><input type="radio" name="q<%= index %>" value="<%= opt1 %>" required> <%= opt1 %></label>
                            <% } %>
                            <% if (opt2 != null && !opt2.trim().isEmpty()) { %>
                                <label><input type="radio" name="q<%= index %>" value="<%= opt2 %>"> <%= opt2 %></label>
                            <% } %>
                            <% if (opt3 != null && !opt3.trim().isEmpty()) { %>
                                <label><input type="radio" name="q<%= index %>" value="<%= opt3 %>"> <%= opt3 %></label>
                            <% } %>
                            <% if (opt4 != null && !opt4.trim().isEmpty()) { %>
                                <label><input type="radio" name="q<%= index %>" value="<%= opt4 %>"> <%= opt4 %></label>
                            <% } %>
                        </div>
            <%
                        index++;
                    }
            %>
                <input type="hidden" name="totalQuestions" value="<%= index - 1 %>">
                <input type="submit" value="Submit Quiz">
            <%
                } else {
            %>
                <p style="color: red;">No questions available. Please contact the administrator.</p>
            <%
                }
            %>
        </form>
    </div>
</body>
</html>

