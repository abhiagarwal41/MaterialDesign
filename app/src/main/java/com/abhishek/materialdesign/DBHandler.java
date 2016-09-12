package com.abhishek.materialdesign;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by sony on 08-09-2016.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "abhishek.db";
    public static final String TABLE_NAME = "MOVIE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_POSTER_PATH  = "POSTER_PATH";
    public static final String COLUMN_RELEASE_DATE = "RELEASE_DATE";
    public static final String COLUMN_OVERVIEW = "OVERVIEW";
    public static final String COLUMN_RATING = "RATING";
    private SQLiteDatabase database;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_TITLE + " VARCHAR, " + COLUMN_POSTER_PATH + " VARCHAR,"+ COLUMN_RELEASE_DATE + " VARCHAR,"+
                COLUMN_OVERVIEW + " VARCHAR,"+ COLUMN_RATING + " REAL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertRecord(Movie movie) {
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, movie.id);
        contentValues.put(COLUMN_TITLE, movie.original_title);
        contentValues.put(COLUMN_POSTER_PATH, movie.poster_path);
        contentValues.put(COLUMN_RELEASE_DATE, movie.release_date);
        contentValues.put(COLUMN_OVERVIEW, movie.overview);
        contentValues.put(COLUMN_RATING, movie.vote_average);
        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public void insertAllRecords(ArrayList<Movie> movies) {
        database = this.getWritableDatabase();
        for(Movie movie:movies) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_ID, movie.id);
            contentValues.put(COLUMN_TITLE, movie.original_title);
            contentValues.put(COLUMN_POSTER_PATH, movie.poster_path);
            contentValues.put(COLUMN_RELEASE_DATE, movie.release_date);
            contentValues.put(COLUMN_OVERVIEW, movie.overview);
            contentValues.put(COLUMN_RATING, movie.vote_average);
            database.insert(TABLE_NAME, null, contentValues);
        }
        database.close();
    }

    public void deleteAllRecords() {
        database = this.getWritableDatabase();
        database.delete(TABLE_NAME, null,null);
        database.close();
    }

    public ArrayList<Movie> getAllRecords() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
      //  Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<Movie> movies = new ArrayList<Movie>();
        Movie movie;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                movie = new Movie();
                movie.setId(cursor.getString(0));
                movie.setOriginal_title(cursor.getString(1));
                movie.setPoster_path(cursor.getString(2));
                movie.setRelease_date(cursor.getString(3));
                movie.setOverview(cursor.getString(4));
                movie.setVote_average(cursor.getDouble(5));
                movies.add(movie);
            }
        }
        cursor.close();
        database.close();
        return movies;
    }

    public Movie getMovie(int movieId){
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + "where " + COLUMN_ID + "=" + movieId, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Movie movie = new Movie();
            movie.setId(cursor.getString(0));
            movie.setOriginal_title(cursor.getString(1));
            movie.setPoster_path(cursor.getString(2));
            movie.setRelease_date(cursor.getString(3));
            movie.setOverview(cursor.getString(4));
            movie.setVote_average(cursor.getDouble(5));
            return movie;
        }
        else
            return null;
    }


}
