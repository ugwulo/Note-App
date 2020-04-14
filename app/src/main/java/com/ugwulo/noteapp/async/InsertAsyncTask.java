package com.ugwulo.noteapp.async;

import android.os.AsyncTask;
import android.util.Log;

import com.ugwulo.noteapp.models.Note;
import com.ugwulo.noteapp.persistence.NoteDao;

public class InsertAsyncTask extends AsyncTask<Note, Void, Void> {
    private static final String TAG = "InsertAsyncTask";
    private NoteDao mNoteDao;

    public InsertAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        Log.d(TAG, "doInBackgroungThread: " + Thread.currentThread().getName());
        mNoteDao.insertNotes(notes);
        return null;
    }
}
