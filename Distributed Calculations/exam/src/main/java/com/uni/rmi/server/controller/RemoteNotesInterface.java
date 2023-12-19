package com.uni.rmi.server.controller;

import com.uni.common.Note;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemoteNotesInterface extends Remote{

    void addNote(Note note) throws RemoteException;
    Note getNoteById(String noteId) throws RemoteException;
    List<Note> getAllNotes() throws RemoteException;
    void updateNote(Note updateNote) throws RemoteException;
    void deleteNoteById(String noteId) throws RemoteException;

    void sortNotesByTime() throws RemoteException;

    void sortNotesByImportance() throws RemoteException;

    void sortNotesByTitle() throws RemoteException;

    void sortNotesById() throws RemoteException;
}
