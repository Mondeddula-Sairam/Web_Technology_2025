<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quiz Result</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f8ff;
            padding: 40px;
            text-align: center;
        }
        .result-box {
            display: inline-block;
            padding: 30px;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 0 10px #ccc;
        }
        .score {
            font-size: 24px;
            color: #4caf50;
        }
        .nav-links {
            margin-top: 20px;
        }
        .nav-links a {
            margin: 0 10px;
            text-decoration: none;
            color: #2196f3;
            font-weight: bold;
        }
        .nav-links a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="result-box">
        <h2>Quiz Submitted!</h2>
        <p class="score">Your Score: <%= session.getAttribute("score") %> / 10</p>
        
        <div class="nav-links">
            <a href="dashboard.jsp">Go to Dashboard</a>
            |
            <a href="logout.jsp">Logout</a>
        </div>
    </div>
</body>
</html>
