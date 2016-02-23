package com.example.maks.notes.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.example.maks.notes.R;
import com.example.maks.notes.adapters.NoteAdapter;
import com.example.maks.notes.constants.Constants;
import com.example.maks.notes.data.Note;
import com.example.maks.notes.database.DB;
import com.example.maks.notes.database.NoteData;
import com.example.maks.notes.database.NoteDataDao;
import com.example.maks.notes.interfaces.OnNoteAddListener;
import com.example.maks.notes.interfaces.OnNoteUpdateListener;
import com.example.maks.notes.tools.NoteDialog;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity implements OnNoteAddListener, OnNoteUpdateListener {

    private RecyclerView rvNotes;
    private BackendlessCollection<Note> noteCollection;
    private ArrayList<Note> noteArrayList;
    private NoteAdapter noteAdapter;
    private ProgressDialog progressDialog;
    private Activity context;
    private SearchView searchView;
    private SharedPreferences dataSave;
    private String whereClause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        context = this;
        initUI();
        setData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                noteArrayList.clear();
                if (s.length() > 0) {
                    noteArrayList.addAll(searchNotes(s));
                } else {

                    noteArrayList.addAll(noteCollection.getData());
                }
                noteAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    private ArrayList<Note> searchNotes(String search) {
        ArrayList<Note> searchableList = new ArrayList<Note>();
        for (Note note : noteCollection.getData()) {
            if (note.getTitle().contains(search)) {
                searchableList.add(note);
            }
        }
        return searchableList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNoteAdd(Note note) {
        progressDialog.setTitle("Creating note");
        progressDialog.show();
        note.saveAsync(new AsyncCallback<Note>() {
            @Override
            public void handleResponse(Note note) {
                NoteDataDao noteDataDao = DB.getDaoMaster().newSession().getNoteDataDao();
                NoteData noteData = new NoteData(note.getObjectId(),
                        note.getOwnerId(),
                        note.getTitle(),
                        note.getDescription(),
                        note.getNote_date(),
                        note.getUpdated(),
                        note.getCreated(),
                        note.getDeleted());
                noteDataDao.insert(noteData);

                noteCollection.getData().add(note);
                noteArrayList.add(note);
                noteAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(context, R.string.note_creation_error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onNoteUpdate(Note note) {

        progressDialog.setTitle("Updating note");
        progressDialog.show();

        noteCollection.getData().add(null);
        noteCollection.getData().remove(null);
        noteArrayList.add(null);
        noteArrayList.remove(null);

        note.saveAsync(new AsyncCallback<Note>() {
            @Override
            public void handleResponse(Note note) {
                NoteDataDao noteDataDao = DB.getDaoMaster().newSession().getNoteDataDao();
                NoteData noteData =

                        new NoteData(note.getObjectId(),
                                note.getOwnerId(),
                                note.getTitle(),
                                note.getDescription(),
                                note.getNote_date(),
                                note.getUpdated(),
                                note.getCreated(),
                                note.getDeleted());
                noteDataDao.insertOrReplace(noteData);

                noteAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                progressDialog.dismiss();
                Toast.makeText(context, R.string.note_update_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        progressDialog.setTitle("Downloading notes");
        progressDialog.show();
        BackendlessDataQuery query = new BackendlessDataQuery();
        query.setWhereClause(whereClause);
        noteArrayList = new ArrayList<Note>();
        Note.findAsync(query, new AsyncCallback<BackendlessCollection<Note>>() {
            @Override
            public void handleResponse(BackendlessCollection<Note> noteBackendlessCollection) {
                noteCollection = noteBackendlessCollection;
                NoteDataDao noteDataDao = DB.getDaoMaster().newSession().getNoteDataDao();
                NoteData noteData;
                for (Note note : noteCollection.getData()) {
                    noteArrayList.add(note);
                    noteData = new NoteData(note.getObjectId(),
                            note.getOwnerId(),
                            note.getTitle(),
                            note.getDescription(),
                            note.getNote_date(),
                            note.getUpdated(),
                            note.getCreated(),
                            note.getDeleted());
                    noteDataDao.insertOrReplace(noteData);
                }
                noteAdapter = new NoteAdapter(noteArrayList, context);
                rvNotes.setLayoutManager(new LinearLayoutManager(context));
                rvNotes.setAdapter(noteAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(context, R.string.note_list_loading_error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    private void initUI() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvNotes = (RecyclerView) findViewById(R.id.rv_notes);
        progressDialog = new ProgressDialog(this, android.R.style.Theme_Holo_Light);
        progressDialog.setCancelable(false);
        dataSave = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
        whereClause = "ownerId = '" + dataSave.getString(Constants.USER_ID, "") + "'";

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                DialogFragment newFragment = NoteDialog.createNote();
                newFragment.show(ft, "note_dialog");

            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                progressDialog.setTitle("Deleting note");
                progressDialog.show();
                final Note note = noteCollection.getData().get(viewHolder.getAdapterPosition());
                note.removeAsync(new AsyncCallback<Long>() {
                    @Override
                    public void handleResponse(Long aLong) {
                        NoteDataDao noteDataDao = DB.getDaoMaster().newSession().getNoteDataDao();
                        NoteData noteData = noteDataDao.load(note.getObjectId());
                        noteDataDao.delete(noteData);
                        noteCollection.getData().remove(note);
                        noteArrayList.remove(note);
                        noteAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(context, R.string.note_delete_error, Toast.LENGTH_SHORT).show();
                        noteArrayList.remove(note);
                        noteArrayList.add(viewHolder.getAdapterPosition(), note);
                        noteAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                });
            }
        });
        itemTouchHelper.attachToRecyclerView(rvNotes);
    }

}
