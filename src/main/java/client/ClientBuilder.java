package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientBuilder implements IClientBuilder {

    private Socket socket;
    private int port;
    private String ip;
    private static Scanner scanner = new Scanner(System.in);

    @Override
    public IClientBuilder setConnection() {
        System.out.println("Set your IpAddress: ");
        ip = scanner.nextLine();
        System.out.println("Set your port: ");
        port = scanner.nextInt();
        try {
            this.socket = new Socket(ip, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Client build() {
        return new Client(socket);
    }


}
