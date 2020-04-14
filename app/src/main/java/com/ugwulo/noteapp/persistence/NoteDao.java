package com.ugwulo.noteapp.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ugwulo.noteapp.models.Note;

import java.util.List;

import static android.icu.text.MessagePattern.ArgType.SELECT;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getNotes();

    @Update
    int update(Note... notes);

    @Delete
    int delete(Note... notes);

    @Insert
    long[] insertNotes(Note... notes);

}
