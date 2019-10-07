package com.vovamisjul.chatlogic;

import com.vovamisjul.chatlogic.user.AbstractUser;
import com.vovamisjul.chatlogic.user.Agent;
import com.vovamisjul.chatlogic.user.Client;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DialogTest {

    @Test
    public void sendToAndPollFrom() {
        int id = Users.addNewUser("agent", "001");
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
        Users.addNewUser("agent", "001");
        int id = Users.addNewUser("client", "Ivan");
        Dialog dialog = Users.getDialog(id);
        dialog.exit("agent");
        AbstractUser user = Users.getUser(id);
        assertEquals(new Client("Ivan", id), user);
    }

    @Test
    public void leave() {
        Users.addNewUser("agent", "001");
        int idNewAgent = Users.addNewUser("agent", "002");
        int id = Users.addNewUser("client", "Ivan");
        Dialog dialog = Users.getDialog(id);
        dialog.leave();
        Dialog dialog2 = Users.getDialog(id);
        assertEquals(new Agent("002", idNewAgent), dialog2.getAgent());
    }
}