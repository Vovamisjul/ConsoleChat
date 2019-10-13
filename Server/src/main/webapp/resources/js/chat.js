function init() {
    let ajax = new Ajax(new User());
    setInterval(ajax.getMessage.bind(ajax), 500);
    $("#register .sendForm").click(function () {
        ajax.register.bind(ajax)();
    });
    $("#dialog .message").keydown(function keyDown(e) {
        if (e.keyCode === 13)
            ajax.sendMessage.bind(ajax)();
    });
    $("#sendMsg").click(function () {
        ajax.sendMessage.bind(ajax)();
    });
    $("#leave").click(function () {
        ajax.leave.bind(ajax)();
    });
    $("#exit").click(function () {
        ajax.exit.bind(ajax)();
    });
}