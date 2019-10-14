<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/jquery-ui.css" rel="stylesheet">
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-3.4.1.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/sha512.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/user.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/controller.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/chat.js"></script>
    <title>Chat</title>
</head>
<body onload="init()">
    <h1>Chat</h1>
    <div style="display: none" id="dialog">
        <div class="correspondence">
        </div>
        <div class="message">
            <input/>
            <button id="sendMsg">Send</button>
        </div>
        <div class="control">
            <button id="leave">Leave</button>
            <button id="exit">Exit</button>
        </div>
    </div>
    <div style="display: flex">
        <div id="register" class="form">
            <p>REGISTRATION</p>
            <p>
                <input id="reg_name" name="name" placeholder="Your name"/>
            </p>
            <p>
                <input id="reg_password" name="password" type="password" placeholder="Your password"/>
            </p>
            <p>
                <select id="reg_type" name="type">
                    <option value="agent">Agent</option>
                    <option value="client">Client</option>
                </select>
            </p>
            <p>
                <button class="sendForm">Register</button>
            </p>
        </div>
        <div id="login" class="form">
            <p>LOGIN</p>
            <p>
                <input id="login_name" name="name" placeholder="Your name"/>
            </p>
            <p>
                <input id="login_password" name="password" type="password" placeholder="Your password"/>
            </p>
            <p>
                <button class="sendForm">Login</button>
            </p>
        </div>
    </div>
</body>
</html>