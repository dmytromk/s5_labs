package com.uni.rmi.client;

import com.uni.common.InputManager;
import com.uni.common.Note;
import com.uni.rmi.server.controller.RemoteNotesInterface;

import java.io.IOException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.util.List;

public class Client {
    private RemoteNotesInterface remoteNotesInterface;
    private final InputManager manager = new InputManager();

    public Client(){
        try{
            Registry reg = LocateRegistry.getRegistry(1234);
            this.remoteNotesInterface = (RemoteNotesInterface) reg.lookup("Airservice");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void printNotes(List<Note> notes){
        if(notes == null){
            return;
        }
        for(Note note : notes){
            System.out.println(note.toString());
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
        note.setName(manager.getString("Enter name: "));
        note.setCountry(manager.getString("Enter country: "));
        return note;
    }

    private Note modifyNote(Note note) {
        System.out.println("\nYou are in an note modification menu");
        System.out.println("Current state : \n" + note);
        while (manager.getBoolean("Do you want change something? ")) {
            System.out.println(" n - name;\n c - country;");
            String input = manager.getString("Enter command : ");
            switch (input) {
                case "n" -> note.setName(manager.getString("Enter name : "));
                case "c" -> note.setCountry(manager.getString("Enter country : "));
                default -> System.out.println("Invalid command!");
            }
        }
        return note;
    }

    private void showNotes() throws IOException {
        List<Note> notes = remoteNotesInterface.getAllNotes();
        for (Note note: notes) {
            System.out.println(note.toString());
        }
    }

    private void addNote() throws IOException {
        Note note = createNote();
        remoteNotesInterface.addNote(note);
    }

    private void getNote() throws IOException {
        String noteId = manager.getString("Enter note ID: ");
        Note note = remoteNotesInterface.getNoteById(noteId);
        if (note != null) {
            System.out.println(note);
        } else {
            System.out.println("No note with such an ID");
        }
    }

    private void updateNote() throws IOException {
        String noteId = manager.getString("Enter note ID: ");
        Note note = remoteNotesInterface.getNoteById(noteId);
        if (note != null) {
            remoteNotesInterface.updateNote(modifyNote(note));
        } else {
            System.out.println("No note with such an ID");
        }
    }

    private void deleteNote() throws IOException {
        String noteId = manager.getString("Enter note ID: ");
        remoteNotesInterface.deleteNoteById(noteId);
    }

    private void loop() throws IOException {
        while (true) {
            String input;
            input = manager.getString("Enter command : ");
            switch (input) {
                case "sa" -> showNotes();
                case "aa" -> addNote();
                case "ga" -> getNote();
                case "ua" -> updateNote();
                case "da" -> deleteNote();
                case "h" -> printAvailableCommands();
                case "q", "e", "exit", "quit" -> {
                    System.out.println("\nExiting...\n");
                    return;
                }
                default -> System.out.println("Invalid command!");
            }
        }
    }

    public static void main(String[] args){
        Client client = new Client();
        try {
            client.printAvailableCommands();
            client.loop();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
