<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.4.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/user.js"></script>
    <script src="${pageContext.request.contextPath}/js/messages.js"></script>
    <title>Chat</title>
</head>
<body onload="init_chat()">
    <h1>Chat</h1>
    <div id="dialog">
        <div class="correspondence">
            <p>Type "/reg [client/agent] [nickname]" to start chat</p>
        </div>
        <div class="message">
            <input/>
            <button onclick="sendMessage()">Send</button>
        </div>
    </div>
</body>
</html>