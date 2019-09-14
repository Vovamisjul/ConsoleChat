package com.vovamisjul.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Redirector extends Thread {
    private BufferedReader clientIn;
    private BufferedWriter clientOut;
    private String userId;
    private String userType;
    private HttpClient client = HttpClientBuilder.create().build();
    protected static final Logger logger = LogManager.getLogger(Redirector.class);

    public Redirector(Socket user) {
        try {
            logger.info("client: " + user + " registered");
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
                String parameters = (userId == null && userType == null) ? "" : ("?userType=" + userType + "&userId=" + userId);
                HttpGet request = new HttpGet(url+parameters);
                HttpResponse response = client.execute(request);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String messageJson = rd.readLine();
                ArrayList<Message> messages = new Gson().fromJson(messageJson, new TypeToken<ArrayList<Message>>(){}.getType());
                if (messages != null) {
                    for (Message message:messages
                         ) {
                        clientOut.write(message.from+": "+message.text+"\n");
                        clientOut.flush();
                    }
                }
                response.getEntity().getContent().close();
                Thread.sleep(500);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
        getMessages.start();
        try {
            while (true) {
                String url = "http://localhost:8081/unnamed/UserReceiver";
                String clientReq = clientIn.readLine();
                HttpPost request = new HttpPost(url);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                if (userType != null) {
                    params.add(new BasicNameValuePair("userType", userType));
                    params.add(new BasicNameValuePair("userId", userId));
                }
                params.add(new BasicNameValuePair("message", clientReq));
                request.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse response = client.execute(request);
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                if (rd.ready()) {
                    String messageJson = rd.readLine();
                    Responce userResponce = new Gson().fromJson(messageJson, new TypeToken<Responce>() {
                    }.getType());
                    if (userResponce.code == 406) {
                        clientOut.write(userResponce.message + "\n");
                    }
                    if (userResponce.code == 200) {
                        userId = userResponce.userId;
                        userType = userResponce.userType;
                    }
                }
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

class Message {
    public String from;
    public String text;

    public Message(String from, String text) {
        this.from = from;
        this.text = text;
    }
}

class Responce {
    public int code;
    public String message;
    public String userId;
    public String userType;
}