package so.pretty.cam_memory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by isp on 16.05.2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_WORDS = "words";
    public static final String COLUMN_WORD = "word";
    public static final int VERSION = 5;
    public static final String WORDS_DB = "words.db";

    public static synchronized List<String> getRandomWords(final Context context, final int count) {
        //TODO ahdd dictionary
        DatabaseHelper helperInstance = getInstance(context);
        Cursor cursor = helperInstance.getWritableDatabase().query(TABLE_WORDS, null, null, null, null, null, null);
        int cCount = cursor.getCount();
        ArrayList<String> words = new ArrayList<String>(count);
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition((int) ((Math.random() * cCount) % cCount));
            words.add(cursor.getString(cursor.getColumnIndex(COLUMN_WORD)));
        }
        cursor.close();
        return words;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table words(word TEXT not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists words;");
        onCreate(db);
    }

    public static synchronized boolean isEmpty(final Context context) {
        DatabaseHelper helperInstance = getInstance(context);
        Cursor cursor = helperInstance.getWritableDatabase().query(TABLE_WORDS, null, null, null, null, null, null);
        int cCount = cursor.getCount();
        return (cCount == 0);
    }

    public static synchronized void init(final Context context) {
        SQLiteDatabase writableDatabase = getInstance(context).getWritableDatabase();

        try {
            InputStream open = context.getAssets().open("word_rus.txt");
            Scanner scanner = new Scanner(open);
            writableDatabase.beginTransaction();
            while (scanner.hasNext()) {
                String next = scanner.next();
                ContentValues values = new ContentValues();
                values.put(COLUMN_WORD, next);
                writableDatabase.insert(TABLE_WORDS, null, values);
            }
            writableDatabase.setTransactionSuccessful();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writableDatabase.endTransaction();
        }
    }


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private static volatile DatabaseHelper instance;

    public static DatabaseHelper getInstance(final Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                instance = new DatabaseHelper(context, WORDS_DB, null, VERSION);
            }
        }
        return instance;
    }

}
