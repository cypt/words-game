package so.pretty.cam_memory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by isp on 16.05.2014.
 */
public class WordHelper extends SQLiteOpenHelper {

    public static List<String> getRandomWords(final Context context, final int count) {
        //TODO ahdd dictionary
        WordHelper helperInstance = getInstance(context);
        Cursor cursor = helperInstance.getReadableDatabase().query("words", null, null, null, null, null, null);
        int cCount = cursor.getCount();
        ArrayList<String> words = new ArrayList<String>(count);
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition((int) ((Math.random() * cCount) % cCount));
            words.add(cursor.getString(cursor.getColumnIndex("word")));
        }
        return words;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if exists words(word TEXT not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table id exists words;");
        onCreate(db);
    }

    public void init(final Context context) {
        SQLiteDatabase writableDatabase = getWritableDatabase();

        try {
            InputStream open = context.getAssets().open("word_rus.txt");
            Scanner scanner = new Scanner(open);
            writableDatabase.beginTransaction();
            while (scanner.hasNext()) {
                String next = scanner.next();
                ContentValues values = new ContentValues();
                values.put("word", next);
                writableDatabase.insert("word", null, values);
            }
            writableDatabase.setTransactionSuccessful();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writableDatabase.close();
        }
    }


    public WordHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private static volatile WordHelper instance;

    public static WordHelper getInstance(final Context context) {
        if (instance == null) {
            synchronized (WordHelper.class) {
                instance = new WordHelper(context, "words.db", null, 1);
                instance.init(context);
            }
        }
        return instance;
    }

}
