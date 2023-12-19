package com.uni.rmi.server.controller;

import com.uni.common.Note;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RemoteNotesController extends UnicastRemoteObject implements RemoteNotesInterface {
    private List<Note> noteList;

    public RemoteNotesController() throws RemoteException {
        super();
        this.noteList = new CopyOnWriteArrayList<>();
    }

    public void addNote(Note note) throws RemoteException {
        noteList.add(note);
    }

    public Note getNoteById(String noteId) throws RemoteException {
        for (Note note : noteList) {
            if (noteId.equals(note.getId())) {
                return note;
            }
        }
        return null;
    }

    public List<Note> getAllNotes() throws RemoteException {
        return noteList;
    }

    public void updateNote(Note updateNote) throws RemoteException {
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

    public void deleteNoteById(String noteId) throws RemoteException {
        noteList.removeIf(note -> noteId.equals(note.getId()));
    }

    public void sortNotesByTime() throws RemoteException {
        noteList.sort(Comparator.comparing(Note::getLastModified));
    }

    public void sortNotesByImportance() throws RemoteException {
        noteList.sort(Comparator.comparingInt(Note::getImportance));
    }

    public void sortNotesByTitle() throws RemoteException {
        noteList.sort(Comparator.comparing(Note::getTitle));
    }

    public void sortNotesById() throws RemoteException {
        noteList.sort(Comparator.comparing(Note::getId));
    }
}
