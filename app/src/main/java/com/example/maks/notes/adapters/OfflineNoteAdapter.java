package com.example.maks.notes.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.maks.notes.R;
import com.example.maks.notes.database.NoteData;

import java.util.ArrayList;

public class OfflineNoteAdapter extends RecyclerView.Adapter<OfflineNoteAdapter.OfflineViewHolder> {

    ArrayList<NoteData> notes;

    public OfflineNoteAdapter(ArrayList<NoteData> noteList) {
        notes = noteList;
    }

    @Override
    public OfflineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_offline_note, parent, false);
        OfflineViewHolder viewHolder = new OfflineViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OfflineViewHolder holder, int position) {
        NoteData note = notes.get(position);
        holder.title.setText(note.getTitle());
        holder.description.setText(note.getDescription());
        holder.date.setText(note.getNote_date());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class OfflineViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView date;

        public OfflineViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_item_offline_title);
            description = (TextView) itemView.findViewById(R.id.tv_item_offline_description);
            date = (TextView) itemView.findViewById(R.id.tv_item_offline_date);
        }
    }
}
