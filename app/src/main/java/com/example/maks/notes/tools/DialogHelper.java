package com.example.maks.notes.tools;

import android.app.AlertDialog;
import android.content.Context;

public class DialogHelper {
    public static AlertDialog createErrorDialog(Context context, String title, String message) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
    }

    public static AlertDialog createNoteDialog(Context context, String title, String message) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();
    }
}
