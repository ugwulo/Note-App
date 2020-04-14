package com.ugwulo.noteapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.ugwulo.noteapp.adapters.NoteRecyclerAdapter;
import com.ugwulo.noteapp.models.Note;
import com.ugwulo.noteapp.persistence.NoteRepository;
import com.ugwulo.noteapp.utils.VerticalSpacingItemDecorator;

import java.util.ArrayList;
import java.util.List;

public class NoteListActivity extends AppCompatActivity implements NoteRecyclerAdapter.OnNoteListener,
        FloatingActionButton.OnClickListener{
    private String TAG = "NotelistActivity";
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NoteRecyclerAdapter mNoteRecyclerAdapter ;
    private NoteRepository mNoteRepository;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        mNoteRepository = new NoteRepository(this);

        findViewById(R.id.fab).setOnClickListener(this);
        mRecyclerView = findViewById(R.id.recyclerView);
        initRecyclerView();
        //insertFakeNotes();
        retrieveNotes();
        Log.d(TAG, "onCreate: onCreate called " + Thread.currentThread().getName());
        setSupportActionBar((Toolbar)findViewById(R.id.notes_toolbar));
        setTitle("Notes");


        dl = (DrawerLayout)findViewById(R.id.drawer_layout);
        t = new ActionBarDrawerToggle(this, dl,R.string.nav_app_bar_open_drawer_description, R.string.navigation_drawer_close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.about:
                        Intent intent = new Intent(NoteListActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    case R.id.tools:
                        Toast.makeText(NoteListActivity.this, "Goan buy tools",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.home:
                        intent = new Intent(NoteListActivity.this, NoteListActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        return true;
                }


                return true;

            }
        });
    }

    public void retrieveNotes(){
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if(mNotes.size() > 0){
                    mNotes.clear();
                }
                if(notes != null){
                    mNotes.addAll(notes);
                }
                mNoteRecyclerAdapter.notifyDataSetChanged();

            }
        });
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void insertFakeNotes(){
        for(int i = 0; i < 1000; i++){
            Note note = new Note();
            note.setTitle("Title # " + i);
            note.setContent("Content # " + i);
            note.setTimeStamp("Dec 2020");
            mNotes.add(note);
        }
        mNoteRecyclerAdapter.notifyDataSetChanged();
    }
    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mNoteRecyclerAdapter = new NoteRecyclerAdapter(mNotes, this);
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);
    }

    @Override
    public void onNoteClick(int position) {
        Log.d(TAG, "onNoteClick: onNote Clicked " + position);
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("selected_note", mNotes.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }
    private void deleteNote(Note note){
        mNotes.remove(note);
        mNoteRecyclerAdapter.notifyDataSetChanged();
        mNoteRepository.deleteNote(note);
    }

    private  ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };
}
