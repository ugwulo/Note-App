package com.ugwulo.noteapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ugwulo.noteapp.R;
import com.ugwulo.noteapp.models.Note;
import com.ugwulo.noteapp.utils.Utility;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {
    private ArrayList<Note> mNotes = new ArrayList<>();
    private OnNoteListener onNoteListener;

    public NoteRecyclerAdapter(ArrayList<Note> notes, OnNoteListener onNoteListener){
        this.mNotes = notes;
        this.onNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_note_list_item, parent, false);
        return new ViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         try {
             String month = mNotes.get(position).getTimeStamp().substring(0, 2);
             month = Utility.getMonthFromNumber(month);
             String year = Utility.getCurrentTimeStamp().substring(3);
             String timeStamp = month + " " + year;
             holder.title.setText(mNotes.get(position).getTitle());
             holder.timeStamp.setText(timeStamp);
         }catch(NullPointerException e){
                 Log.e(TAG, "onBindViewHolder: NullPointerException " + e.getMessage());
         }

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, timeStamp;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener){
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            timeStamp = itemView.findViewById(R.id.note_time_stamp);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }


    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
