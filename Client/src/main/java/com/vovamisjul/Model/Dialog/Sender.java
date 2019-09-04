package com.vovamisjul.Model.Dialog;

import com.vovamisjul.Model.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

public class Sender extends Thread {
    private Dialog dialog;
    private BufferedWriter writerServer;
    private Scanner in;

    public Sender(Dialog dialog, BufferedWriter writerServer, Scanner in) {
        this.dialog = dialog;
        this.writerServer = writerServer;
        this.in = in;
    }
    @Override
    public void run() {
        try {
            while (!dialog.isEnd()) {
                writerServer.write(in.nextLine()+'\n');
                writerServer.flush();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        finally {
            dialog.setEnd(true);
            try {
                writerServer.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
