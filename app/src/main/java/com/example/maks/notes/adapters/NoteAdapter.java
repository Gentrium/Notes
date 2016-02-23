package com.example.maks.notes.adapters;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maks.notes.R;
import com.example.maks.notes.data.Note;
import com.example.maks.notes.tools.NoteDialog;

import java.util.ArrayList;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private ArrayList<Note> notes;
    private Activity context;

    public NoteAdapter(ArrayList<Note> notes, Activity context) {
        this.notes = notes;
        this.context = context;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_note,parent,false);
        NoteViewHolder viewHolder = new NoteViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        final Note note = notes.get(position);
        holder.title.setText(note.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = context.getFragmentManager().beginTransaction();
                Fragment prev = context.getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                DialogFragment newFragment = NoteDialog.openNote(note.getObjectId());
                newFragment.show(ft, "note_dialog");
            }
        });
        holder.date.setText(note.getNote_date());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView date;
        public NoteViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_item_title);
            date = (TextView) itemView.findViewById(R.id.tv_item_date);
        }
    }
}
