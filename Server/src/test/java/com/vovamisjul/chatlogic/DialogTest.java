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
        int id = 0;
        users.addNewUser("agent", "001", id);
        users.addNewUser("client", "Ivan", 1);
        Dialog dialog = users.getDialog(id);
        dialog.sendTo("agent", "Hello!");
        Message message = dialog.pollFrom("client");
        Message message2 = dialog.pollFrom("client");
        assertEquals("Server" + "You chat now with Agent 001" + "001" + "Hello!",
                message.from+message.text+message2.from+message2.text);
    }

    @Test
    public void exit() {
        int idAgent = 0;
        users.addNewUser("agent", "001", idAgent);
        int idClient = 1;
        users.addNewUser("client", "Ivan", idClient);
        Dialog dialog = users.getDialog(idClient);
        dialog.exit("agent", users);
        AbstractUser user = users.getUser(idClient);
        assertEquals(new Client("Ivan", idClient), user);
    }

    @Test
    public void leave() {
        int idAgent = 0;
        users.addNewUser("agent", "001", idAgent);
        int idAgent2 = 2;
        users.addNewUser("agent", "002", idAgent2);
        int idClient = 1;
        users.addNewUser("client", "Ivan", idClient);
        Dialog dialog = users.getDialog(idClient);
        dialog.leave(users);
        Dialog dialog2 = users.getDialog(idClient);
        assertEquals(new Agent("002", idAgent2), dialog2.getAgent());
    }
}