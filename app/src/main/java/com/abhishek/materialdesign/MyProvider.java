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
    private static final String MOVIES_BASE_PATH = "movie";
    static final String URL = "content://" + AUTHORITY + "/" + MOVIES_BASE_PATH;
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final int MOVIES = 100;
    public static final int MOVIE_ID = 110;

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, MOVIES_BASE_PATH, MOVIES);
        sURIMatcher.addURI(AUTHORITY, MOVIES_BASE_PATH + "/#", MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        dbHandler = new DBHandler(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)){
            /**
             * Get all movies records
             */
            case MOVIES:
                return "vnd.android.cursor.dir/vnd.example.students";

            /**
             * Get a particular movie
             */
            case MOVIE_ID:
                return "vnd.android.cursor.item/vnd.example.students";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DBHandler.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case MOVIE_ID:
                queryBuilder.appendWhere(DBHandler.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            case MOVIES:
                // no filter
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
        /**
         * Add a new movie record
         */
        long rowID = dbHandler.getWritableDatabase().insert(	dbHandler.TABLE_NAME, "", values);

        /**
         * If record is added successfully
         */

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
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
