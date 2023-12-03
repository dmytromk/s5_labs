package com.uni.socket.server;

import com.uni.common.controller.ConnectionPool;
import com.uni.common.controller.DatabaseController;
import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Server {
    private ServerSocket serverSocket;
    @Getter
    private DatabaseController databaseController;
    private ConnectionPool connectionPool;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            // TODO: change to real url
            this.connectionPool = new ConnectionPool("localhost", "user", "password");
            this.databaseController = new DatabaseController(this.connectionPool);
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
            closeServer();
        }
    }

    public void start() {
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("\n A new Client has connected!\n");
                RequestHandler handler = new RequestHandler(this, socket);

                Thread handlerThread = new Thread(handler);
                handlerThread.start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                closeServer();
            }
        }
    }

    private void closeServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
