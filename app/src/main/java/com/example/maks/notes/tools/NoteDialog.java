package com.example.maks.notes.tools;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.maks.notes.R;
import com.example.maks.notes.activities.NotesActivity;
import com.example.maks.notes.data.Note;
import com.example.maks.notes.interfaces.OnNoteAddListener;
import com.example.maks.notes.interfaces.OnNoteUpdateListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteDialog extends DialogFragment {

    private String noteId = "no_id";
    private Note mNote;
    private TextView tvTitle;
    private EditText etTitle;
    private EditText etDescription;
    private EditText etDate;
    private Button btnAccept;
    private Button btnCancel;

    public static NoteDialog openNote(String id) {
        NoteDialog f = new NoteDialog();

        Bundle args = new Bundle();
        args.putString("id", id);
        f.setArguments(args);

        return f;
    }

    public static NoteDialog createNote() {
        return new NoteDialog();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteId = getArguments().getString("id");
        }

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.note_fragment, container, false);

        etDescription = (EditText) v.findViewById(R.id.et_frag_description);
        etDate = (EditText) v.findViewById(R.id.et_frag_date);
        btnAccept = (Button) v.findViewById(R.id.btn_frag_ok);
        btnCancel = (Button) v.findViewById(R.id.btn_frag_cancel);

        tvTitle = (TextView) v.findViewById(R.id.tv_frag_title);
        etTitle = (EditText) v.findViewById(R.id.et_frag_title);

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (noteId.equals("no_id")) {
            noteCreation();
        } else {
            openedNote();
        }
        return v;
    }

    private void openedNote() {
        Note.findByIdAsync(noteId, new AsyncCallback<Note>() {
            @Override
            public void handleResponse(Note note) {
                mNote = note;
                getDialog().setTitle(mNote.getTitle());
                btnCancel.setText(R.string.ok);
                btnAccept.setText(R.string.edit);

                etDescription.setFocusable(false);
                etDate.setFocusable(false);
//
                etDescription.setText(mNote.getDescription());
                etDate.setText(mNote.getNote_date());

                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editNote();
                    }
                });
            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Toast.makeText(getActivity(), R.string.note_loading_error, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

    }

    private void noteCreation() {

        tvTitle.setVisibility(View.VISIBLE);
        etTitle.setVisibility(View.VISIBLE);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        final String date = format.format(new Date(System.currentTimeMillis()));
        etDate.setText(date);
        etDate.setFocusableInTouchMode(false);
        etDate.setFocusable(false);



        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                if (title.length() < 1 || title.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), R.string.enter_title, Toast.LENGTH_SHORT).show();
                    return;
                }
                String description = etDescription.getText().toString();

                mNote = new Note();
                mNote.setDescription(description);
                mNote.setTitle(title);
                mNote.setNote_date(date);
                OnNoteAddListener addNoteListener = (NotesActivity) getActivity();
                addNoteListener.onNoteAdd(mNote);
                dismiss();
            }
        });
    }

    private void editNote() {
        getDialog().setTitle("Editing: " + mNote.getTitle());
        btnCancel.setText(R.string.cancel);
        btnAccept.setText(R.string.confirm);

        etDescription.setFocusable(true);
        etDescription.setFocusableInTouchMode(true);
        etDescription.requestFocus();

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = etDescription.getText().toString();
                mNote.setDescription(description);

                OnNoteUpdateListener updateListener = (NotesActivity) getActivity();
                updateListener.onNoteUpdate(mNote);
                dismiss();
            }
        });
    }
}
