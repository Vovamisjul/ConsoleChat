<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/jquery-ui.css" rel="stylesheet">
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-3.4.1.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery-ui.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/User.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/Ajax.js"></script>
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
    <div id="register">
        <p>REGISTRATION</p>
        <p>
            <input class="name" name="name" placeholder="Your name"/>
        </p>
        <p>
            <select class="type" name="type">
                <option value="agent">Agent</option>
                <option value="client">Client</option>
            </select>
        </p>
        <p>
            <button class="sendForm">Register</button>
        </p>
    </div>
</body>
</html>