class Ajax {
    constructor(user) {
        this.user = user;
    }
    register() {
        $.ajax({
            url: "/chat/register",
            type: "POST",
            data:{name: $("#register .name").val(), type: $("#register .type").val()},
            success: function (data) {
                $("#register").hide();
                $("#dialog").show();
                this.user.register(data.userId, data.userType);
                let paragraph = "<p>" + data.message + "</p>";
                $("#dialog .correspondence").empty();
                $("#dialog .correspondence").append(paragraph);
            }.bind(this),
            dataType: "json"
        })
    }
    sendMessage() {
        let input = $("#dialog input");
        let message = input.val();
        let yourMessage = "<p> You: " + message + "</p>";
        $("#dialog .correspondence").append(yourMessage);
        input.val("");
        $.ajax({
            url:"/chat/sendMessage",
            type:"POST",
            data:{userType: this.user.type, message: message, userId: this.user.id}
        });
    }
    getMessage() {
        if (this.user.isRegistered())
            $.ajax({
                url: "/chat/getMessages",
                type: "GET",
                data: {userType: this.user.type, userId: this.user.id},
                success: function (message) {
                    for (let i in message) {
                        let paragraph = "<p>" + message[i].from + ": " + message[i].text + "</p>";
                        $("#dialog .correspondence").append(paragraph);
                    }
                },
                dataType: "json"
            });
    }
    leave() {
        if (this.user.isRegistered())
            $.ajax({
                url: "/chat/leave",
                type: "POST",
                data: {userId: this.user.id}
            });
    }
    exit() {
        if (this.user.isRegistered())
            $.ajax({
                url: "/chat/exit",
                type: "POST",
                data: {userType: this.user.type, userId: this.user.id},
                success: function () {
                    this.user.unregister();
                }.bind(this)
            });
    }
}