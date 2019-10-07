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

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Redirector extends Thread {
    private BufferedReader clientIn;
    private BufferedWriter clientOut;
    private int userId;
    private String userType;
    private boolean registered = false;
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
            String url = "http://localhost:8081/chat/getMessages";
            while (true) {
                try {
                    if (registered) {
                        String parameters = "?userType=" + userType + "&userId=" + userId;
                        HttpGet request = new HttpGet(url + parameters);
                        HttpResponse response = client.execute(request);
                        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        String messageJson = rd.readLine();
                        ArrayList<Message> messages = new Gson().fromJson(messageJson, new TypeToken<ArrayList<Message>>() {
                        }.getType());
                        if (messages != null) {
                            for (Message message : messages
                            ) {
                                clientOut.write(message.from + ": " + message.text + "\n");
                                clientOut.flush();
                            }
                        }
                        response.getEntity().getContent().close();
                    }
                Thread.sleep(500);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
        getMessages.start();
        try {
            while (true) {
                String clientReq = clientIn.readLine();
                if (!registered) {
                    if (clientReq.startsWith("/register")) {
                        String[] params = clientReq.substring("/register ".length()).split(" ");
                        register(params[0], params[1]);
                    }
                    else clientOut.write("No such command\n");
                }
                else {
                    switch (clientReq) {
                        case "/leave":
                            leave();
                            break;
                        case "/exit":
                            exit();
                            break;
                        default: sendMessage(clientReq);
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

    private void register(String type, String name) {
        try {
            HttpPost request = new HttpPost("http://localhost:8081/chat/register");
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("type", type));
            request.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String messageJson = rd.readLine();
            Responce userResponse = new Gson().fromJson(messageJson, new TypeToken<Responce>() {
            }.getType());
            clientOut.write(userResponse.message + "\n");
            userId = userResponse.userId;
            userType = userResponse.userType;
            registered = true;
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void sendMessage(String clientReq) {
        try {
            HttpPost request = new HttpPost("http://localhost:8081/chat/sendMessage");
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("userId", String.valueOf(userId)));
            params.add(new BasicNameValuePair("userType", userType));
            params.add(new BasicNameValuePair("message", clientReq));
            request.setEntity(new UrlEncodedFormEntity(params));
            client.execute(request);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void exit() {
        try {
            HttpPost request = new HttpPost("http://localhost:8081/chat/exit");
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("userId", String.valueOf(userId)));
            params.add(new BasicNameValuePair("userType", userType));
            request.setEntity(new UrlEncodedFormEntity(params));
            client.execute(request);
            registered = false;
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void leave() {
        try {
            HttpPost request = new HttpPost("http://localhost:8081/chat/leave");
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("userId", String.valueOf(userId)));
            request.setEntity(new UrlEncodedFormEntity(params));
            client.execute(request);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
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
    public String message;
    public int userId;
    public String userType;
}