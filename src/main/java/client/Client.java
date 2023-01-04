package client;

import logger.Logger;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
        try {
            Scanner scanner = new Scanner(System.in);
            if (username == null) {
                System.out.println("Set your nickname: ");
                username = scanner.next();
                bufferedWriter.write(username);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

            while (socket.isConnected()) {
                String msgToSend = scanner.nextLine();
                if(!msgToSend.equals("")){
                    bufferedWriter.write(username + ": " + msgToSend);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }

            }
        } catch (IOException e) {
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public static void readSettings(){
        try(FileReader reader = new FileReader("C:\\Users\\lshap\\IdeaProjects\\server\\settings.txt"))
        {
            int c;
            StringBuilder stringBuilder = new StringBuilder();
            while((c=reader.read())!=-1){
                stringBuilder.append((char)c);
            }
            System.out.println(stringBuilder.toString().trim() + " для подключения к серверу");
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }


    public void listenForMsg() {
        new Thread(() -> {
            String msgFromGroupChat;
            while (socket.isConnected()) {
                try {
                    msgFromGroupChat = bufferedReader.readLine();
                    System.out.println(formatForDateNow.format(dateNow) + " " + msgFromGroupChat);
                    logger.log(formatForDateNow.format(dateNow) + " " + msgFromGroupChat);

                } catch (IOException e) {
                    closeEverything(socket, bufferedWriter, bufferedReader);
                }
            }
        }).start();

    }

    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Client.readSettings();
        Client client = new ClientBuilder()
                .setConnection()
                .build();
        client.listenForMsg();
        client.sendMsg();


    }


}
