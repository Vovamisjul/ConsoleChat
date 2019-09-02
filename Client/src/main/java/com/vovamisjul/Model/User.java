package com.vovamisjul.Model;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class User {
    private Scanner in = new Scanner(System.in);
    private BufferedReader readerServer;
    private BufferedWriter writerServer;
    private boolean endDialog = false;

    public void start() {
        System.out.println("Enter ip address and port of server");
        try {
            String address = in.nextLine();
            int port = Integer.parseInt(in.nextLine());
            try {
                Socket clientSocket = new Socket(address, port);
                readerServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writerServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                dialog();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void dialog() {
        System.out.println("Enter /register [role] [name] to start");
        new Thread(() -> {
            try {
                while (!endDialog) {
                    writerServer.write(in.nextLine()+'\n');
                    writerServer.flush();
                }
            } catch (IOException e) {
                endDialog = true;
                System.out.println(e.getMessage());
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!endDialog) {
                        String message = readerServer.readLine();
                        if (message==null) {
                            endDialog = true;
                            System.out.println("Your interlocutor ends dialog. Press enter to exit");
                            in.close();
                            writerServer.close();
                            readerServer.close();
                            break;
                        }
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    endDialog = true;
                    System.out.println(e.getMessage());
                }
            }
        }).start();
    }
}
