package com.vovamisjul.Model;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;

import java.io.*;
import java.net.Socket;

public class Redirector extends Thread {
    private BufferedReader clientIn;
    private BufferedWriter clientOut;
    private int userId = 0;
    private String userType = "client";
    protected static final Logger logger = LogManager.getLogger(Redirector.class);

    public Redirector(Socket user) {
        try {
            clientIn = new BufferedReader(new InputStreamReader(user.getInputStream()));
            clientOut = new BufferedWriter(new OutputStreamWriter(user.getOutputStream()));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void run() {
        Thread getMessages = new Thread(()-> {
            String url = "http://localhost:8081/unnamed/GetMessage";
            while (true) {
                try {
                HttpClient client = HttpClientBuilder.create().build();
                String parameters = (userId==-1 && userType == null) ? "" : ("?userType=" + userType + "&userId=" + userId);
                HttpGet request = new HttpGet(url+parameters);
                HttpResponse response = client.execute(request);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    System.out.println(line);
                }
                Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        getMessages.start();
        try {
            while (true) {
                String clientReq = clientIn.readLine();

            }
        }
        catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            try {
                clientIn.close();
                clientOut.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
