package com.vovamisjul.Model.Dialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Scanner;

public class Dialog {
    private BufferedReader readerServer;
    private BufferedWriter writerServer;
    private Scanner in;
    private boolean end = false;

    public Dialog(BufferedReader readerServer, BufferedWriter writerServer, Scanner in) {
        this.readerServer = readerServer;
        this.writerServer = writerServer;
        this.in = in;
    }

    public void start() {
        System.out.println("Enter /register [role] [name] to start");
        new Sender(this, writerServer, in).start();
        new Recipient(this, readerServer).start();
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }
}
