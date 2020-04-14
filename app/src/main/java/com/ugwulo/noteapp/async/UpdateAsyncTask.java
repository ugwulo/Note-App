package com.ugwulo.noteapp.async;

import android.os.AsyncTask;
import android.util.Log;

import com.ugwulo.noteapp.models.Note;
import com.ugwulo.noteapp.persistence.NoteDao;

public class UpdateAsyncTask extends AsyncTask<Note, Void, Void> {
    private static final String TAG = "DeleteAsyncTask";
    private NoteDao mNoteDao;

    public UpdateAsyncTask(NoteDao dao) {
        mNoteDao = dao;
    }

    @Override
    protected Void doInBackground(Note... notes) {
        Log.d(TAG, "doInBackgroungThread: " + Thread.currentThread().getName());
        mNoteDao.update(notes);
        return null;
    }
}