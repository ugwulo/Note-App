package com.ugwulo.noteapp.async;

import android.os.AsyncTask;
import android.util.Log;

import com.ugwulo.noteapp.models.Note;
import com.ugwulo.noteapp.persistence.NoteDao;

public class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {
    private static final String TAG = "DeleteAsyncTask";
    private NoteDao mNoteDao;

    public DeleteAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        Log.d(TAG, "doInBackgroungThread: " + Thread.currentThread().getName());
        mNoteDao.delete(notes);
        return null;
    }
}