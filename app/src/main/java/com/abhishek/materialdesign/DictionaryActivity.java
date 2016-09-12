package com.abhishek.materialdesign;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.UserDictionary;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class DictionaryActivity extends AppCompatActivity {

    // For the SimpleCursorAdapter to match the UserDictionary columns to layout items.
    private static final String[] COLUMNS_TO_BE_BOUND  = new String[] {
            UserDictionary.Words.WORD,
            UserDictionary.Words.FREQUENCY
    };

    private static final int[] LAYOUT_ITEMS_TO_FILL = new int[] {
            android.R.id.text1,
            android.R.id.text2
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

    /*    // Get the TextView which will be populated with the Dictionary ContentProvider data.
        TextView dictTextView = (TextView) findViewById(R.id.dictionary_text_view);

        // Get the ContentResolver which will send a message to the ContentProvider
        ContentResolver resolver = getContentResolver();

        // Get a Cursor containing all of the rows in the Words table
        Cursor cursor = resolver.query(UserDictionary.Words.CONTENT_URI, null, null, null, null);

        // Surround the cursor in a try statement so that the finally block will eventually execute
        try {
            dictTextView.setText("The UserDictionary contains " + cursor.getCount() + " words\n");
            dictTextView.append("COLUMNS: " + UserDictionary.Words._ID  + " - " + UserDictionary.Words.FREQUENCY +
                    " - " + UserDictionary.Words.WORD);

            // Get the index of the columns using UserDictionary.Words constants,
            // which contain the headers of the columns.
            int idColumn = cursor.getColumnIndex(UserDictionary.Words._ID);
            int frequencyColumn = cursor.getColumnIndex(UserDictionary.Words.FREQUENCY);
            int wordColumn = cursor.getColumnIndex(UserDictionary.Words.WORD);

            // Iterates through all returned rows in the cursor.
            while (cursor.moveToNext()) {
                // Use that index to extract the String value of the word
                // at the current row the cursor is on.
                int id = cursor.getInt(idColumn);
                int frequency = cursor.getInt(frequencyColumn);
                String word = cursor.getString(wordColumn);

                dictTextView.append(("\n" + id + " - " + frequency + " - " + word));
            }
        } finally {
            // Always close your cursor to avoid memory leaks
            cursor.close();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get the TextView which will be populated with the Dictionary ContentProvider data.
        ListView dictListView = (ListView) findViewById(R.id.dictionary_list_view);

        // Get the ContentResolver which will send a message to the ContentProvider.
        ContentResolver resolver = getContentResolver();

        // Get a Cursor containing all of the rows in the Words table.
        Cursor cursor = resolver.query(UserDictionary.Words.CONTENT_URI, null, null, null, null);

        // Set the Adapter to fill the standard two_line_list_item layout with data from the Cursor.
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.two_line_list_item,
                cursor,
                COLUMNS_TO_BE_BOUND,
                LAYOUT_ITEMS_TO_FILL,
                0);

        // Attach the adapter to the ListView.
        dictListView.setAdapter(adapter);
    }
}
