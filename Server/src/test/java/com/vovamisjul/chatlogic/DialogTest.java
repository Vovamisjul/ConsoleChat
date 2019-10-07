package com.vovamisjul.chatlogic;

import com.vovamisjul.chatlogic.user.AbstractUser;
import com.vovamisjul.chatlogic.user.Agent;
import com.vovamisjul.chatlogic.user.Client;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DialogTest {

    private Users users;
    @Before
    public void setUp() {
        users = new Users();
    }

    @Test
    public void sendToAndPollFrom() {
        int id = users.addNewUser("agent", "001");
        users.addNewUser("client", "Ivan");
        Dialog dialog = users.getDialog(id);
        dialog.sendTo("agent", "Hello!");
        Message message = dialog.pollFrom("client");
        Message message2 = dialog.pollFrom("client");
        assertEquals("Server" + "You chat now with Agent 001" + "001" + "Hello!",
                message.from+message.text+message2.from+message2.text);
    }

    @Test
    public void exit() {
        users.addNewUser("agent", "001");
        int id = users.addNewUser("client", "Ivan");
        Dialog dialog = users.getDialog(id);
        dialog.exit("agent", users);
        AbstractUser user = users.getUser(id);
        assertEquals(new Client("Ivan", id), user);
    }

    @Test
    public void leave() {
        users.addNewUser("agent", "001");
        int idNewAgent = users.addNewUser("agent", "002");
        int id = users.addNewUser("client", "Ivan");
        Dialog dialog = users.getDialog(id);
        dialog.leave(users);
        Dialog dialog2 = users.getDialog(id);
        assertEquals(new Agent("002", idNewAgent), dialog2.getAgent());
    }
}