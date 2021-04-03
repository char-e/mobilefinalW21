package com.kakaladies.newsdemon;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ArticleRepository {

    private final DatabaseHelper helper;
    private final Context context;
    private SQLiteDatabase db;

    public ArticleRepository(Context context) {
        this.helper = new DatabaseHelper(context);
        this.context = context;
    }

    /**
     * Save a message to the database.
     *
     * @param
     * @return the saved message, with an id
     */
    public Article save(Article article) {
        db = helper.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put(DatabaseHelper.COL_TEXT, article.getHeadline());

        row.put(DatabaseHelper.COL_DESC, article.getDescription());

        long id = db.insert(DatabaseHelper.TABLE_NAME, null, row);
        db.close();
        return new Article();
    }

    /**
     * Retrieve all messages.
     *
     * @return a set of all messages
     */
    public List<Article> findAll() {
        db = helper.getReadableDatabase();
        String[] columns = {
                DatabaseHelper.COL_ID,

                DatabaseHelper.COL_TEXT
        };
        Cursor results = db.query(
                false,
                DatabaseHelper.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null,
                null
        );
        List<Article> articles = new ArrayList<>(results.getCount());
        if (results.getCount() > 0) {
            while (results.moveToNext()) {
                Article article;
                article = new Article();


                articles.add(article);
            }
            results.moveToFirst();
            printCursor(results, db.getVersion());
        }
        // Avoid memory leak
        results.close();
        db.close();
        return articles;
    }

    /**
     * Delete a message from the database.
     *
     *
     */
    public void delete(Article article) {
        db = helper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_NAME, "_id=?", new String[]{article.getHeadline()});
        db.close();
    }

    /**
     * Print the cursor information.
     *
     * @param c       cursor
     * @param version version of the database
     */
    private void printCursor(Cursor c, int version) {
        String activity = ((Activity) context).getComponentName().flattenToString();
        StringBuilder columnNames = new StringBuilder();
        Log.i(activity, "Database Version: " + version);
        Log.i(activity, "Number of Columns: " + c.getColumnCount());
        for (int i = 0; i < c.getColumnCount(); i++) {
            columnNames.append(c.getColumnName(i));
            if (i != c.getColumnCount()) {
                columnNames.append(", ");
            }
        }
        Log.i(activity, "Columns: " + TextUtils.join(",", c.getColumnNames()));
        Log.i(activity, "Number of Results: " + c.getCount());
        Log.i(activity, "Results:\n");
        while (c.moveToNext()) {
            StringBuilder resultRow = new StringBuilder();
            for (int i = 0; i < c.getColumnCount(); i++) {
                resultRow
                        .append(c.getColumnName(i))
                        .append(": ")
                        .append(c.getString(i))
                        .append("; ");
            }
            Log.i(activity, resultRow.toString());
        }
    }
}