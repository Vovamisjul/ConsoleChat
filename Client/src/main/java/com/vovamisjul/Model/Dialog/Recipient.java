package com.vovamisjul.Model.Dialog;

import com.vovamisjul.Model.User;

import java.io.BufferedReader;
import java.io.IOException;

public class Recipient extends Thread {
    private Dialog dialog;
    private BufferedReader readerServer;

    public Recipient(Dialog dialog, BufferedReader readerServer) {
        this.dialog = dialog;
        this.readerServer = readerServer;
    }
    @Override
    public void run() {
        try {
            while (!dialog.isEnd()) {
                String message = readerServer.readLine();
                if (message==null) {
                    System.out.println("Your interlocutor ends dialog. Press enter to exit");
                    break;
                }
                System.out.println(message);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            dialog.setEnd(true);
            try {
                readerServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
