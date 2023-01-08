package client;

import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientBuilder implements IClientBuilder {

    private Socket socket;
    private int port;
    private String ip;
    private static final String FILE_SETTINGS = "src\\main\\java\\settings.txt";

    @Override
    public IClientBuilder setConnection() {
        try(FileReader reader = new FileReader(FILE_SETTINGS))
        {
            StringBuilder stringBuilder = new StringBuilder();
            int c;
            while((c=reader.read())!=-1){
                stringBuilder.append ((char)c);
            }
            List<String> settings = Arrays.asList(stringBuilder.toString().split("\r\n"));
            port = Integer.parseInt(settings.get(0).substring(5));
            ip = settings.get(1).substring(3);
            this.socket = new Socket(ip, port);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        return this;
    }

    @Override
    public Client build() {
        return new Client(socket);
    }


}
