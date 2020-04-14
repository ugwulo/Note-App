package com.ugwulo.noteapp.persistence;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.ugwulo.noteapp.async.DeleteAsyncTask;
import com.ugwulo.noteapp.async.InsertAsyncTask;
import com.ugwulo.noteapp.async.UpdateAsyncTask;
import com.ugwulo.noteapp.models.Note;

import java.util.List;

public class NoteRepository {
    private NoteDatabase mNoteDatabase;
    public NoteRepository(Context context){
        mNoteDatabase = NoteDatabase.getInstance(context);
    }

    public void deleteNote(Note note){
        new DeleteAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }
    public void updateNote(Note note){
        new UpdateAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public void insertNotesTask(Note note){
        new InsertAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public LiveData<List<Note>> retrieveNotesTask(){
        return mNoteDatabase.getNoteDao().getNotes();}

}
