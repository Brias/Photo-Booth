package de.braeuer.matthias.photobooth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Matze on 21.06.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PhotoBooth.db";

    private static final String QUERY_GET_ALL = "SELECT * FROM ";
    private static final String QUERY_DELETE_WHERE = PhotoBoothDBContract.PhotoBoothEntry.COLUMN_NAME_IMAGE + " = ? ";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PhotoBoothDBContract.PhotoBoothEntry.TABLE_NAME + " (" +
                    PhotoBoothDBContract.PhotoBoothEntry._ID + " INTEGER PRIMARY KEY," +
                    PhotoBoothDBContract.PhotoBoothEntry.COLUMN_NAME_IMAGE + TEXT_TYPE + COMMA_SEP +
                    PhotoBoothDBContract.PhotoBoothEntry.COLUMN_NAME_EMAIL + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PhotoBoothDBContract.PhotoBoothEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean insertImage(String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(PhotoBoothDBContract.PhotoBoothEntry.COLUMN_NAME_IMAGE, name);
        contentValues.put(PhotoBoothDBContract.PhotoBoothEntry.COLUMN_NAME_EMAIL, email);

        long inserted = db.insert(PhotoBoothDBContract.PhotoBoothEntry.TABLE_NAME, null, contentValues);

        return inserted != -1;
    }

    public ArrayList<Image> getAllImages() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(QUERY_GET_ALL + PhotoBoothDBContract.PhotoBoothEntry.TABLE_NAME, null);

        ArrayList<Image> images = new ArrayList<>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {

                while (!cursor.isAfterLast()) {
                    Image image = new Image();

                    String email = cursor.getString(2);
                    String name = cursor.getString(1);

                    image.setEmail(email);
                    image.setName(name);

                    images.add(image);

                    cursor.moveToNext();
                }
            }
        }

        return images;
    }

    public boolean deleteImage(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        int deleted = db.delete(PhotoBoothDBContract.PhotoBoothEntry.TABLE_NAME, QUERY_DELETE_WHERE, new
                String[]{name});

        return deleted != 0;
    }
}
