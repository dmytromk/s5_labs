package dmytromk;

import java.io.*;
import java.net.*;

public class Server {
    private static SerializableObject receivedObject;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server is listening on port 12345");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

            receivedObject = (SerializableObject) objectInputStream.readObject();
            System.out.println("Object received: " + receivedObject);

            objectInputStream.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static SerializableObject getReceivedObject() {
        return receivedObject;
    }
}


