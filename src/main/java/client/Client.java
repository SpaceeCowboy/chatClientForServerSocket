package client;

import logger.Logger;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Client {
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Socket socket;
    private String username;
    private Date dateNow = new Date();
    private SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss a");
    private static final Logger logger = new Logger();

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            closeEverything(socket, bufferedWriter, bufferedReader);
        }

    }

    public void sendMsg() {
        try (Scanner scanner = new Scanner(System.in)) {
            if (username == null && socket.isConnected()) {
                System.out.println("Set your nickname: ");
                username = scanner.next();
                bufferedWriter.write(username);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            while (!socket.isClosed()) {
                String msgToSend = scanner.nextLine();
                if (!msgToSend.equals("") && msgToSend != null) {
                    bufferedWriter.write(username + ": " + msgToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }


    public void listenForMsg() {
        new Thread(() -> {
            String msgFromGroupChat;
            while (socket.isConnected()) {
                try {
                    msgFromGroupChat = bufferedReader.readLine();
                    if (msgFromGroupChat == null) {
                        System.out.println("Нажмите Enter для завершения работы");
                        closeEverything(socket, bufferedWriter, bufferedReader);
                        break;
                    }
                    System.out.println(formatForDateNow.format(dateNow) + " " + msgFromGroupChat);
                    if (!username.equals(Arrays.stream(msgFromGroupChat.trim().split(":")).toList().get(0))) {
                        logger.log(formatForDateNow.format(dateNow) + " " + msgFromGroupChat);
                    }

                } catch (IOException e) {
                    closeEverything(socket, bufferedWriter, bufferedReader);
                }
            }
        }).start();

    }

    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try {
            if (socket != null) {
                socket.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new ClientBuilder()
                .setConnection()
                .build();
        System.out.println("Для выхода из чата введите <exit>");
        client.listenForMsg();
        client.sendMsg();

    }


}
