package com.uni.rmi.client;

import com.uni.common.InputManager;
import com.uni.common.Note;
import com.uni.rmi.server.controller.RemoteNotesInterface;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.util.Comparator;
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
        System.out.println("sort_time - Sort Notes by Time");
        System.out.println("sort_importance - Sort Notes by Importance");
        System.out.println("sort_title - Sort Notes by Title");
        System.out.println("sort_id - Sort Notes by ID");
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

    public void sortNotesByTime() throws RemoteException {
        remoteNotesInterface.sortNotesByTime();
    }

    public void sortNotesByImportance() throws RemoteException {
        remoteNotesInterface.sortNotesByImportance();
    }

    public void sortNotesByTitle() throws RemoteException {
        remoteNotesInterface.sortNotesByTitle();
    }

    public void sortNotesById() throws RemoteException {
        remoteNotesInterface.sortNotesById();
    }

    private void loop() throws IOException {
        while (true) {
            String input;
            input = manager.getString("Enter command : ");
            switch (input) {
                case "show" -> showNotes();
                case "add" -> addNote();
                case "get" -> getNote();
                case "update" -> updateNote();
                case "delete" -> deleteNote();
                case "sort_time" -> sortNotesByTime();
                case "sort_importance" -> sortNotesByImportance();
                case "sort_title" -> sortNotesByTitle();
                case "sort_id" -> sortNotesById();
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
