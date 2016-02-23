package com.example.maks.notes.data;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.geo.GeoPoint;
import com.backendless.persistence.BackendlessDataQuery;

public class Note {
    private String deleted;
    private java.util.Date created;
    private String description;
    private String objectId;
    private String title;
    private String note_date;
    private String ownerId;
    private java.util.Date updated;

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public java.util.Date getCreated() {
        return created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectId() {
        return objectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote_date() {
        return note_date;
    }

    public void setNote_date(String note_date) {
        this.note_date = note_date;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public java.util.Date getUpdated() {
        return updated;
    }


    public Note save() {
        return Backendless.Data.of(Note.class).save(this);
    }

    public Future<Note> saveAsync() {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<Note> future = new Future<Note>();
            Backendless.Data.of(Note.class).save(this, future);

            return future;
        }
    }

    public void saveAsync(AsyncCallback<Note> callback) {
        Backendless.Data.of(Note.class).save(this, callback);
    }

    public Long remove() {
        return Backendless.Data.of(Note.class).remove(this);
    }

    public Future<Long> removeAsync() {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<Long> future = new Future<Long>();
            Backendless.Data.of(Note.class).remove(this, future);

            return future;
        }
    }

    public void removeAsync(AsyncCallback<Long> callback) {
        Backendless.Data.of(Note.class).remove(this, callback);
    }

    public static Note findById(String id) {
        return Backendless.Data.of(Note.class).findById(id);
    }

    public static Future<Note> findByIdAsync(String id) {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<Note> future = new Future<Note>();
            Backendless.Data.of(Note.class).findById(id, future);

            return future;
        }
    }

    public static void findByIdAsync(String id, AsyncCallback<Note> callback) {
        Backendless.Data.of(Note.class).findById(id, callback);
    }

    public static Note findFirst() {
        return Backendless.Data.of(Note.class).findFirst();
    }

    public static Future<Note> findFirstAsync() {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<Note> future = new Future<Note>();
            Backendless.Data.of(Note.class).findFirst(future);

            return future;
        }
    }

    public static void findFirstAsync(AsyncCallback<Note> callback) {
        Backendless.Data.of(Note.class).findFirst(callback);
    }

    public static Note findLast() {
        return Backendless.Data.of(Note.class).findLast();
    }

    public static Future<Note> findLastAsync() {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<Note> future = new Future<Note>();
            Backendless.Data.of(Note.class).findLast(future);

            return future;
        }
    }

    public static void findLastAsync(AsyncCallback<Note> callback) {
        Backendless.Data.of(Note.class).findLast(callback);
    }

    public static BackendlessCollection<Note> find(BackendlessDataQuery query) {
        return Backendless.Data.of(Note.class).find(query);
    }

    public static Future<BackendlessCollection<Note>> findAsync(BackendlessDataQuery query) {
        if (Backendless.isAndroid()) {
            throw new UnsupportedOperationException("Using this method is restricted in Android");
        } else {
            Future<BackendlessCollection<Note>> future = new Future<BackendlessCollection<Note>>();
            Backendless.Data.of(Note.class).find(query, future);

            return future;
        }
    }

    public static void findAsync(BackendlessDataQuery query, AsyncCallback<BackendlessCollection<Note>> callback) {
        Backendless.Data.of(Note.class).find(query, callback);
    }
}