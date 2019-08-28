package com.vovamisjul.Model;

import org.junit.Test;

import static org.junit.Assert.*;
import java.io.*;

public class UserTestClientOk {

    @Test
    public void start() {
        InputStream in = new ByteArrayInputStream("127.0.0.1\r\n1111\r\n/adsasdads\r\n/register client vasya\r\nhi\r\n/exit\r\n\r\n".getBytes());
        System.setIn(in);
        OutputStream output = new ByteArrayOutputStream(1000);
        OutputStream expected = new ByteArrayOutputStream(1000);
        try {
            expected.write(("Enter ip address and port of server\r\nEnter /register [role] [name] to start\r\nThere are no such command\r\nPrint your message to start\r\nYour interlocutor ends dialog. Press enter to exit\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintStream out = new PrintStream(output);
        System.setOut(out);
        new User().start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(expected.toString(), output.toString());
    }
}