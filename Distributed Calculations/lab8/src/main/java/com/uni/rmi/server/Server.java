package com.uni.rmi.server;

import com.uni.common.controller.ConnectionPool;
import com.uni.common.controller.DatabaseController;
import com.uni.rmi.server.database.RemoteDatabaseController;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

public class Server {
    private RemoteDatabaseController remoteDatabaseController;


    public Server() {
        try {
        String url = "jdbc:postgresql://localhost:5432/lab8";
        ConnectionPool connectionPool = new ConnectionPool(url, "postgres", "");
        DatabaseController databaseController = new DatabaseController(connectionPool);
        this.remoteDatabaseController = new RemoteDatabaseController(databaseController);
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void start(int port) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind("Airtable", remoteDatabaseController);

        System.out.println("SERVER ONLINE");
    }

    public static void main(String[] args){
        Server server = new Server();
        try {
            server.start(1234);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

    }
}
