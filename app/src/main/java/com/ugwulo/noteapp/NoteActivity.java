package com.ugwulo.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import com.ugwulo.noteapp.models.Note;
import com.ugwulo.noteapp.persistence.NoteRepository;
import com.ugwulo.noteapp.utils.Utility;

public class NoteActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher
{
    private String TAG = "NoteActivity";
    private static final int EDIT_MODE_DISABLED = 0;
    private static final int EDIT_MODE_ENABLED = 1;
    //ui
    private LinedEditText  mLinedEditText;
    private EditText mTextEditTitle;
    private TextView mViewTitle;
    private RelativeLayout mCheckContainer, mBackContainer;
    private ImageButton mBackArrow, mCheckMark;
    //var
    private boolean mIsNewNote;
    Note mInitialNote;
    Note mFinalNote;
    GestureDetector mGestureDetector;
    private int mMode;
    private NoteRepository mNoteRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mLinedEditText = findViewById(R.id.note_text);
        mViewTitle = findViewById(R.id.text_note_title);
        mTextEditTitle = findViewById(R.id.text_edit_note);
        mBackArrow = findViewById(R.id.toolbar_back_arrow);
        mCheckMark = findViewById(R.id.toolbar_check_mark);
        mBackContainer = findViewById(R.id.toolbar_back_arrow_container);
        mCheckContainer = findViewById(R.id.toolbar_check_mark_container);
        mNoteRepository = new NoteRepository(this);

        if(getIncomingIntent()) {
            enableEditMode();
            setNewNoteProperties();
        }
        else{
            disableContentInteraction();
            hideSoftKeyboard();
            setNoteProperties();
        }
        setListeners();
    }

    private void saveNewNote(){
        mNoteRepository.insertNotesTask(mFinalNote);

    }

    private void saveChanges(){
        if(mIsNewNote){
            saveNewNote();
        }else{
            updateNote();
        }
    }



    private void hideSoftKeyboard(){
        InputMethodManager inputMethodManager =(InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null){
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void disableContentInteraction(){
        mLinedEditText.setFocusable(false);
        mLinedEditText.setFocusableInTouchMode(false);
        mLinedEditText.setCursorVisible(false);
        mLinedEditText.setKeyListener(null);
        mLinedEditText.clearFocus();
    }


    private void enableContentInteraction(){
        mLinedEditText.setFocusable(true);
        mLinedEditText.setFocusableInTouchMode(true);
        mLinedEditText.setCursorVisible(true);
        mLinedEditText.setKeyListener(new EditText(this).getKeyListener());
        mLinedEditText.requestFocus();
    }

    private void enableEditMode(){
        mBackContainer.setVisibility(View.GONE);
        mTextEditTitle.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.VISIBLE);
        mViewTitle.setVisibility(View.GONE);
        mMode = EDIT_MODE_ENABLED;
        enableContentInteraction();
    }

    private void disableEditMode(){
        mBackContainer.setVisibility(View.VISIBLE);
        mTextEditTitle.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.GONE);
        mViewTitle.setVisibility(View.VISIBLE);
        mMode = EDIT_MODE_DISABLED;
        disableContentInteraction();
        String temp = mLinedEditText.getText().toString();
        temp = temp.replace("\n", "" );
        temp = temp.replace(" ", "");

        if(temp.length() > 0){
            mFinalNote.setContent(mLinedEditText.getText().toString());
            mFinalNote.setTitle(mTextEditTitle.getText().toString());
            String timeStamp = Utility.getCurrentTimeStamp();
            mFinalNote.setTimeStamp(timeStamp);
        }

        try {
            if(!mFinalNote.getContent().equals(mInitialNote.getContent())
                    || !mFinalNote.getTitle().equals(mInitialNote.getTitle())){
                saveChanges();
            }

        }catch (NullPointerException e){
            Toast toast = Toast.makeText(getApplicationContext(), "Please, insert a note", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void updateNote(){
        mNoteRepository.updateNote(mFinalNote);
    }
    private void setListeners(){
        mLinedEditText.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this, this);
        mViewTitle.setOnClickListener(this);
        mCheckMark.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mTextEditTitle.addTextChangedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mMode == EDIT_MODE_ENABLED){
            onClick(mCheckMark);
        }
        else{
            super.onBackPressed();
        }
    }

    private void setNoteProperties(){
        mLinedEditText.setText(mInitialNote.getContent());
        mViewTitle.setText(mInitialNote.getTitle());
        mViewTitle.setText(mInitialNote.getTitle());
    }

    private void setNewNoteProperties(){
        mTextEditTitle.setText("Note Title");
        mTextEditTitle.setText("Note Title");

        mFinalNote = new Note();
        mInitialNote = new Note();
        mFinalNote.setTitle("Note Title");
        mInitialNote.setTitle("Note Title");
    }
    private boolean getIncomingIntent(){
        if (getIntent().hasExtra("selected_note")) {
            mInitialNote = getIntent().getParcelableExtra("selected_note");
            mFinalNote =  new Note();
            mFinalNote.setContent(mInitialNote.getContent());
            mFinalNote.setTitle(mInitialNote.getTitle());
            mFinalNote.setTimeStamp(mInitialNote.getTimeStamp());
            mFinalNote.setId(mInitialNote.getId());
            mMode = EDIT_MODE_DISABLED;
            mIsNewNote = false;
            return false;
        }
        mMode = EDIT_MODE_ENABLED;
        mIsNewNote = true;
        return true;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        enableEditMode();
        enableContentInteraction();
        Log.d(TAG, "onDoubleTap: double tapped!");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toolbar_back_arrow:{
                finish();
                break;
            }
            case R.id.toolbar_check_mark: {
                disableEditMode();
                break;
            }
            case R.id.text_note_title: {
                enableEditMode();
                mTextEditTitle.requestFocus();
                mTextEditTitle.setText(mViewTitle.getText());
                mTextEditTitle.setSelection(mTextEditTitle.length());
                break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode", mMode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");
        if(mMode == EDIT_MODE_ENABLED ){
            enableEditMode();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mViewTitle.setText(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
