package com.uni.rmi.server;

import com.uni.rmi.server.controller.RemoteNotesController;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    private RemoteNotesController remoteNotesController;


    public Server() {
        try {
        this.remoteNotesController = new RemoteNotesController();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void start(int port) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind("Airservice", remoteNotesController);

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
