function sendMessage() {
    let input = $("#dialog input");
    let message = input.val();
    let yourMessage = "<p> You: " + message + "</p>"
    $("#dialog .correspondence").append(yourMessage);
    input.val("");
    $.ajax({
        url:"/unnamed/UserReceiver",
        type:"POST",
        data:{userType: userType, message: message, userId: userId},
        success: function (data) {
            if (data.code === 406) {
                let paragraph = "<p>" + data.message + "</p>";
                $("#dialog .correspondence").append(paragraph);
            }
            if (data.code === 200) {
                userId = data.userId;
                userType = data.userType;
            }
        },
        dataType: "json"
    });
}

function getMessages() {
    $.ajax({
        url:"/unnamed/GetMessage",
        type:"GET",
        data:{userType: userType, userId: userId},
        success: function (message) {
            for (let i in message) {
                let paragraph = "<p>" + message[i].from + ": " + message[i].text + "</p>";
                $("#dialog .correspondence").append(paragraph);
            }
        },
        dataType: "json"
    });
}

function init_chat() {
    setInterval(getMessages, 100);
    $("#dialog .message").keydown(function keyDown(e) {
        if (e.keyCode === 13)
            sendMessage();
    });
}