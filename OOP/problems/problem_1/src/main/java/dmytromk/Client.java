package dmytromk;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 12345);

            SerializableObject objectToSend = new SerializableObject("Hello from client!");

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            objectOutputStream.writeObject(objectToSend);
            System.out.println("Object sent: " + objectToSend);

            objectOutputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
