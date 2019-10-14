class Controller {

    token;
    constructor(user) {
        this.user = user;
    }
    register() {
        $.ajax({
            url: "/chat/register",
            type: "POST",
            data:{name: $("#reg_name").val(), password: sha512($("#reg_password").val()), type: $("#reg_type").val()},
            success: function (data) {
                $("#register").hide();
                $("#login").hide();
                $("#dialog").show();
                this.token = data.token;
                this.user.register();
                let paragraph = "<p>" + data.message + "</p>";
                $("#dialog .correspondence").empty();
                $("#dialog .correspondence").append(paragraph);
            }.bind(this),
            error: function (error) {
                alert("This name is busy");
            },
            dataType: "json"
        })
    }
    login() {
        $.ajax({
            url: "/chat/login",
            type: "POST",
            data:{name: $("#login_name").val(), password: sha512($("#login_password").val())},
            success: function (data) {
                $("#register").hide();
                $("#login").hide();
                $("#dialog").show();
                this.token = data.token;
                this.user.register();
                let paragraph = `<p>${data.message}</p>`;
                $("#dialog .correspondence").empty();
                $("#dialog .correspondence").append(paragraph);
            }.bind(this),
            error: function (error) {
                alert("Wrong login or password");
            },
            dataType: "json"
        })
    }
    sendMessage() {
        let input = $("#dialog input");
        let message = input.val();
        let yourMessage = `<p> You: ${message}</p>`;
        $("#dialog .correspondence").append(yourMessage);
        input.val("");
        $.ajax({
            url:"/chat/sendMessage",
            type:"POST",
            data:{message: message},
            headers: {
                'Authorization': `Bearer ${this.token}`
            },
            error: function (error) {
                if (error.status >= 400) {
                    alert("Token time is expired, relogin");
                    location.reload();
                }
            }
        });
    }
    getMessage() {
        if (this.user.isRegistered())
            $.ajax({
                url: "/chat/getMessages",
                type: "GET",
                headers: {
                    'Authorization': `Bearer ${this.token}`
                },
                success: function (message) {
                    for (let i in message) {
                        let paragraph = "<p>" + message[i].from + ": " + message[i].text + "</p>";
                        $("#dialog .correspondence").append(paragraph);
                    }
                },
                dataType: "json",
                error: function (error) {
                    if (error.status >= 400) {
                        alert("Token time is expired, relogin");
                        location.reload();
                    }
                }
            });
    }
    leave() {
        if (this.user.isRegistered())
            $.ajax({
                url: "/chat/leave",
                type: "POST",
                headers: {
                    'Authorization': `Bearer ${this.token}`
                },
                error: function (error) {
                    if (error.status >= 400) {
                        alert("Token time is expired, relogin");
                        location.reload();
                    }
                }
            });
    }
    exit() {
        if (this.user.isRegistered())
            $.ajax({
                url: "/chat/exit",
                type: "POST",
                headers: {
                    'Authorization': `Bearer ${this.token}`
                },
                success: function () {
                    this.user.unregister();
                    $("#register").show();
                    $("#login").show();
                    $("#dialog").hide();
                }.bind(this),
                error: function (error) {
                    if (error.status >= 400) {
                        alert("Token time is expired, relogin");
                        location.reload();
                    }
                }
            });
    }
}