package org.eva.service.impl;

import org.eva.service.HttpServer;
import org.eva.util.HttpHandler;

import java.net.ServerSocket;
import java.net.Socket;

import static org.eva.common.HttpConstants.PORT;

public class HttpServerImpl implements HttpServer {
    @Override
    public void start(int port) {
        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server started on port: " + port);

            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("New connection established");
                new Thread(new HttpHandler(socket)).start();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
