package client;

public interface IClientBuilder {
    IClientBuilder setConnection();
    Client build();

}
