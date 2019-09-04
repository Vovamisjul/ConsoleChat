package com.vovamisjul.Model;

import com.vovamisjul.Model.Dialog.Dialog;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class User {
    private Scanner in = new Scanner(System.in);

    public void start() {
        System.out.println("Enter ip address and port of server");
        try {
            String address = in.nextLine();
            int port = Integer.parseInt(in.nextLine());
            try {
                Socket clientSocket = new Socket(address, port);
                BufferedReader readerServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter writerServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                new Dialog(readerServer, writerServer, in).start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
