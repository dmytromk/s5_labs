package com.uni.socket.server;

import com.uni.common.JsonMapper;
import com.uni.common.Note;

import java.io.*;
import java.net.Socket;
import java.util.Comparator;
import java.util.List;

public class RequestHandler implements Runnable {
    private Socket socket;
    private Server server;
    private BufferedReader reader;
    private PrintWriter writer;

    public RequestHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeClient() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void showNote() throws IOException {
        List<Note> notes = server.getNoteList();
        writer.println(JsonMapper.convertObjectToJson(notes));
    }

    private void getNote() throws IOException {
        String id = reader.readLine();
        List<Note> noteList = server.getNoteList();
        for (Note note : noteList) {
            if (id.equals(note.getId())) {
                writer.println(JsonMapper.convertObjectToJson(note));
            }
        }
        writer.println("");
    }

    private void addNote() throws IOException {
        String value = reader.readLine();
        Note note = JsonMapper.convertJsonToObject(value, Note.class);
        server.getNoteList().add(note);
    }

    private void deleteNote() throws IOException {
        String id = reader.readLine();
        List<Note> noteList = server.getNoteList();
        noteList.removeIf(note -> id.equals(note.getId()));
    }

    private void updateNote() throws IOException {
        String value = reader.readLine();
        Note updateNote = JsonMapper.convertJsonToObject(value, Note.class);
        List<Note> noteList = server.getNoteList();
        for (Note note : noteList) {
            if (note.getId().equals(updateNote.getId())) {
                note.setTitle(updateNote.getTitle());
                note.setContent(updateNote.getContent());
                note.setImportance(updateNote.getImportance());
                note.setLastModified(updateNote.getLastModified());

                break;
            }
        }
    }

    public void sortByLastModified() {
        server.getNoteList().sort(Comparator.comparing(Note::getLastModified));
    }

    public void sortByImportance() {
        server.getNoteList().sort(Comparator.comparingInt(Note::getImportance).reversed());
    }

    public void sortByTitle() {
        server.getNoteList().sort(Comparator.comparing(Note::getTitle));
    }

    public void sortById() {
        server.getNoteList().sort(Comparator.comparing(Note::getId));
    }

    public void run() {
        try {
            while (socket.isConnected() && !socket.isClosed()) {
                String input = reader.readLine();
                writer.flush();
                switch (input) {
                    case "show":
                        showNote();
                        break;
                    case "get":
                        getNote();
                        break;
                    case "add":
                        addNote();
                        break;
                    case "delete":
                        deleteNote();
                        break;
                    case "update":
                        updateNote();
                        break;
                    case "sort_time":
                        sortByLastModified();
                        break;
                    case "sort_importance":
                        sortByImportance();
                        break;
                    case "sort_title":
                        sortByTitle();
                        break;
                    case "sort_id":
                        sortById();
                        break;
                    case "q", "e", "exit", "quit":
                        closeClient();
                        return;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            closeClient();
        }
    }
}
