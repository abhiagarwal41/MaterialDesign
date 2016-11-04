package com.abhishek.materialdesign;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class SimpleWidgetService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this

    public SimpleWidgetService() {
        super("SimpleWidgetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                SimpleWidgetProvider.class));

        // Get today's data from the ContentProvider
        String URL = MyProvider.MOVIES_URI.toString();

        Uri movies_uri = Uri.parse(URL);
        Cursor c = getContentResolver().query(movies_uri, null, null, null, null);
        //Cursor c = getActivity().managedQuery(movies, null, null, null, "name");
        LinkedList<Movie> movies = new LinkedList<>();

        if (c.moveToFirst()) {
            do{
                Movie movie = new Movie();
                movie.setId(c.getString(c.getColumnIndex(DBHandler.COLUMN_ID)));
                movie.setOriginal_title(c.getString(c.getColumnIndex(DBHandler.COLUMN_TITLE)));
                movie.setPoster_path(c.getString(c.getColumnIndex(DBHandler.COLUMN_POSTER_PATH)));
                movie.setRelease_date(c.getString(c.getColumnIndex(DBHandler.COLUMN_RELEASE_DATE)));
                movie.setOverview(c.getString(c.getColumnIndex(DBHandler.COLUMN_OVERVIEW)));
                movie.setVote_average(c.getDouble(c.getColumnIndex(DBHandler.COLUMN_RATING)));
                movies.add(movie);
            } while (c.moveToNext());
        }

        // Perform this loop procedure for each Today widget
        for (int appWidgetId : appWidgetIds) {
            // Find the correct layout based on the widget's width
           /* int widgetWidth = getWidgetWidth(appWidgetManager, appWidgetId);
            int defaultWidth = getResources().getDimensionPixelSize(R.dimen.widget_today_default_width);
            int largeWidth = getResources().getDimensionPixelSize(R.dimen.widget_today_large_width);
            int layoutId;
            if (widgetWidth >= largeWidth) {
                layoutId = R.layout.widget_today_large;
            } else if (widgetWidth >= defaultWidth) {
                layoutId = R.layout.widget_today;
            } else {
                layoutId = R.layout.widget_today_small;
            }*/
            int layoutId = R.layout.simple_widget;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            if(movies!=null) {
                if(movies.size()>0) {
                    Collections.shuffle(movies);
                    Movie movie = movies.get(0);
                    views.setTextViewText(R.id.textView, movie.original_title);
                    //views.setImageViewUri(R.id.imageView,Uri.parse("http://image.tmdb.org/t/p/w185/"+ movie.poster_path));
                    views.setImageViewBitmap(R.id.imageView,getBitmapFromURL("http://image.tmdb.org/t/p/w185/"+ movie.poster_path));
                }
            }


            //views.setImageViewResource(R.id.imageView, R.drawable.profile);

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widgetLayout, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
