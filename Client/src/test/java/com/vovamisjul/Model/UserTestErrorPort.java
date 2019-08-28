package com.vovamisjul.Model;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class UserTestErrorPort {

    @Test
    public void start() {
        InputStream in = new ByteArrayInputStream("127.0.0.1\r\n1112\r\n".getBytes());
        System.setIn(in);
        OutputStream output = new ByteArrayOutputStream();
        OutputStream expected = new ByteArrayOutputStream(1000);
        try {
            expected.write(("Enter ip address and port of server\r\n"+"Connection refused: connect\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintStream out = new PrintStream(output);
        System.setOut(out);
        new User().start();
        assertEquals(output.toString(), expected.toString());
    }
}