package com.vovamisjul.chatlogic;

import com.vovamisjul.chatlogic.user.AbstractUser;
import com.vovamisjul.chatlogic.user.Client;
import org.junit.Test;

import static org.junit.Assert.*;

public class DialogTest {

    @Test
    public void sendToAndPollFrom() {
        final int id = 0;
        Users.addNewUser("agent", "001");
        Users.addNewUser("client", "Ivan");
        Dialog dialog = Users.getDialog(id);
        dialog.sendTo("agent", "Hello!");
        Message message = dialog.pollFrom("client");
        Message message2 = dialog.pollFrom("client");
        assertEquals("Server" + "You chat now with Agent 001" + "001" + "Hello!",
                message.from+message.text+message2.from+message2.text);
    }

    @Test
    public void exit() {
        final int id = 1;
        Users.addNewUser("agent", "001");
        Users.addNewUser("client", "Ivan");
        Dialog dialog = Users.getDialog(id);
        dialog.exit("agent");
        AbstractUser user = Users.getUser(id);
        assertEquals(new Client("Ivan", id), user);
    }

//    @Test
//    public void leave() {
//        final int id = 2;
//        Users.addNewUser("agent", "001");
//        Users.addNewUser("agent", "002");
//        Users.addNewUser("client", "Ivan");
//        Dialog dialog = Users.getDialog(id);
//        dialog.leave();
//        Dialog dialog2 = Users.getDialog(id);
//        assertEquals(new Agent("002", 1), dialog2.getAgent());
//    }
}