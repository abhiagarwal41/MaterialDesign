package com.abhishek.materialdesign;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by sony on 09-09-2016.
 */

public class MyProvider extends ContentProvider {

    private DBHandler dbHandler;
    private static final String AUTHORITY = "com.abhishek.materialdesign";
    private static final String MOVIES_BASE_PATH = DBHandler.TABLE_NAME;
    static final String URL = "content://" + AUTHORITY + "/" + MOVIES_BASE_PATH;
    public static final Uri MOVIES_URI = Uri.parse(URL);

    public static final int MOVIES = 100;

    private static final UriMatcher uriMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, MOVIES_BASE_PATH, MOVIES);
    }

    @Override
    public boolean onCreate() {
        dbHandler = new DBHandler(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all movies records
             */
            case MOVIES:
                return "vnd.android.cursor.dir/vnd.example.students";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = uriMatcher.match(uri);
        switch (uriType) {

            case MOVIES:
                queryBuilder.setTables(DBHandler.TABLE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(dbHandler.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        if(uriMatcher.match(uri)==MOVIES) {
            long rowID = dbHandler.getWritableDatabase().insert(dbHandler.TABLE_NAME, "", values);

            if (rowID > 0)  //If record is added successfully
            {
                Uri _uri = ContentUris.withAppendedId(MOVIES_URI, rowID);
                getContext().getContentResolver().notifyChange(_uri, null);
                return _uri;
            }
            throw new SQLException("Failed to add a record into " + uri);
        }

        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }
}
