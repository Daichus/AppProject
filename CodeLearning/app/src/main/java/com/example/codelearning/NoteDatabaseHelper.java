package com.example.codelearning;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class NoteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_NOTE_ID = "note_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "category"; // 新增分類名稱欄位

    private long currentUserId;

    public NoteDatabaseHelper(Context context, long userId) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.currentUserId = userId; // 將當前用戶 ID 傳入
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NOTES + " ("
                + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_CONTENT + " TEXT, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_ID + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NOTES + " ADD COLUMN " + COLUMN_CATEGORY + " TEXT;");
        }
    }

    public ArrayList<String> getAllCategories(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> categories = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT DISTINCT " + COLUMN_CATEGORY + " FROM " + TABLE_NOTES +
                " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return categories;
    }

}
