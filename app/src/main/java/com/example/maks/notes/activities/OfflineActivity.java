package com.example.maks.notes.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.maks.notes.R;
import com.example.maks.notes.adapters.OfflineNoteAdapter;
import com.example.maks.notes.database.DB;
import com.example.maks.notes.database.NoteData;
import com.example.maks.notes.database.NoteDataDao;

import java.util.ArrayList;

public class OfflineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_offline);
        NoteDataDao noteDataDao = DB.getDaoMaster().newSession().getNoteDataDao();
        ArrayList<NoteData> noteList = (ArrayList<NoteData>) noteDataDao.queryBuilder().list();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new OfflineNoteAdapter(noteList));
    }
}
