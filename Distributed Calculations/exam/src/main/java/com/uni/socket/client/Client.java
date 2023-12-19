package com.uni.socket.client;

import com.uni.common.InputManager;
import com.uni.common.JsonMapper;
import com.uni.common.Note;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

public class Client {
    private InputManager manager = new InputManager();
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean exit = false;

    public Client(String host, int portId) {
        try {
            clientSocket = new Socket(host, portId);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void closeClient() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printAvailableCommands() {
        System.out.println("Available Commands:");
        System.out.println("show - Show Notes");
        System.out.println("get - Get Note");
        System.out.println("add - Add Note");
        System.out.println("delete - Delete Notes");
        System.out.println("update - Update Notes");
        System.out.println("q, e, exit, quit - Quit the application");
    }

    private Note createNote() {
        System.out.println("\nYou are in an note creation menu");
        Note note = new Note();
        System.out.println("New note`s ID is " + note.getId());
        note.setTitle(manager.getString("Enter title: "));
        note.setContent(manager.getString("Enter content: "));
        note.setImportance(manager.getInt("Enter importance: "));
        return note;
    }

    private Note modifyNote(Note note) {
        System.out.println("\nYou are in an note modification menu");
        System.out.println("Current state : \n" + note);
        while (manager.getBoolean("Do you want change something? ")) {
            System.out.println(" t - title;\n c - content;\ni - importance");
            String input = manager.getString("Enter command : ");
            switch (input) {
                case "t" -> note.setTitle(manager.getString("Enter title : "));
                case "c" -> note.setContent(manager.getString("Enter content : "));
                case "i" -> note.setImportance(manager.getInt("Enter importance : "));
                default -> System.out.println("Invalid command!");
            }
        }
        return note;
    }

    private void showNotes() throws IOException {
        out.println("show");
        String result = in.readLine();
        if (Objects.equals(result, "")) {
            return;
        }
        List<Note> notes = JsonMapper.convertJsonToList(result, Note.class);
        for (Note note : notes) {
            System.out.println(note.toString());
        }
    }
    private void addNote() throws IOException {
        out.println("add");
        Note note = createNote();
        out.println(JsonMapper.convertObjectToJson(note));
    }

    private void getNote() throws IOException {
        out.println("get");
        String noteId = manager.getString("Enter note ID: ");
        out.println(noteId);
        String result = in.readLine();
        if (!Objects.equals(result, "")) {
            Note note = JsonMapper.convertJsonToObject(result, Note.class);
            System.out.println(note);
        } else {
            System.out.println("No note with such an ID");
        }
    }

    private void updateNote() throws IOException {
        out.println("get");
        String noteId = manager.getString("Enter note ID: ");
        out.println(noteId);
        String result = in.readLine();
        if (!Objects.equals(result, "")) {
            Note note = JsonMapper.convertJsonToObject(result, Note.class);
            out.println("update");
            out.println(JsonMapper.convertObjectToJson(modifyNote(note)));
        } else {
            System.out.println("No note with such an ID");
        }
    }

    private void deleteNote() throws IOException {
        out.println("delete");
        String noteId = manager.getString("Enter note ID: ");
        out.println(noteId);
    }

    private void loop() throws IOException {
        String input;
        input = manager.getString("Enter command : ");
        switch (input) {
            case "show" -> showNotes();
            case "add" -> addNote();
            case "get" -> getNote();
            case "update" -> updateNote();
            case "delete" -> deleteNote();
            case "h" -> printAvailableCommands();
            case "q", "e", "exit", "quit" -> {
                System.out.println("\nExiting...\n");
                out.println("e");
                exit = true;
                return;
            }
            default -> System.out.println("Invalid command!");
        }
    }

    public void run() {
        try {
            printAvailableCommands();
            while (clientSocket.isConnected() && !clientSocket.isClosed() && !exit) {
                loop();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        closeClient();
    }

    public static void main(String[] args){
        String host = "localhost";
        final int portId = 1234;
        Client client = new Client(host, portId);
        client.run();
    }
}
