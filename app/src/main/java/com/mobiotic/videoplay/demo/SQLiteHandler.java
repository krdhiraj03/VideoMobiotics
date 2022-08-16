package com.mobiotic.videoplay.demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mobiotic.videoplay.demo.model.VideoSQLiteData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Mobiotics";

    private static final String INFO = "videoPlayInfo";

    long Id;

    private Context context;
    private static final String video_id = "v_id";
    private static final String seek_position = "position";
    public static final String ID = "id";


    public static final String CREATE_INFO_TABLE = "CREATE TABLE " + INFO +
            "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            video_id + " TEXT , " + seek_position + " TEXT )";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_INFO_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + INFO);
        onCreate(db);
    }

    public boolean savePlayedVideoInfo(String videoId, String seekPosition){
            SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(video_id, videoId); // category_id
        values.put(seek_position, seekPosition); // category_id
        Id = db.insert(INFO, null, values);
        //db.close(); // Closing database connection
        Log.d(TAG, "New Video Info inserted into sqlite: " + Id);
        Log.d(TAG, "VideoId " + videoId);
        Log.d(TAG, SQLiteHandler.class.getSimpleName() + seekPosition);

        if (Id == -1) {
            return false;
        } else {
            return true;
        }

    }
    public void updatePlayedVideoInfo(String vid,String seekPosition) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(seek_position, seekPosition); // completion_dt
        db.update(INFO, values, video_id + " = " + vid, null);
        Log.d(TAG, "Info updated into sqlite: " + vid);
    }

    public String getData(int id) {
        String valuePos ="";
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuerybooking = "SELECT  position FROM " + INFO +" WHERE "+ video_id +"="+ id;
        Cursor cursor =  db.rawQuery( selectQuerybooking, null );
        if (cursor.moveToFirst()) {
                valuePos =  cursor.getString(cursor.getColumnIndex("position"));
            }
        return valuePos;
    }

    public Cursor check()
    {
        String query = "SELECT  * FROM " + INFO ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public boolean isExist(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + INFO + " WHERE "+" v_id = '" + id + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        db.close();
        return exist;

    }

}
