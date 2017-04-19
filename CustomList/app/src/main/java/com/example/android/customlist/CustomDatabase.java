package com.example.android.customlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.UUID;

import static com.example.android.customlist.CustomContract.CustomEntry.*;

/**
 * Created by 张俊秋 on 2017/4/16.
 */

public class CustomDatabase extends SQLiteOpenHelper {
    private static final int dbVersion=1;
    private static final String dbName="custom.db";

    public CustomDatabase (Context context){
        super(context,dbName,null,dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLENAME+"( "+
                _ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COLUMN_UUID+" TEXT NOT NULL,"+
                COLUMN_NAME+" TEXT NOT NULL, "+
                COLUMN_DESCRIPTION+" TEXT, "+
                COLUMN_LEVEL+ " INTEGER DEFAULT 0, "+
                COLUMN_TIME+ " INTEGER DEFAULT 1," +
                COLUMN_DEVELOP+" INTEGER DEFAULT 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static ContentValues getContentValue(String name,String des,int level,int time,boolean develop){
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_NAME,name);
        contentValues.put(COLUMN_UUID, UUID.randomUUID().toString());
        contentValues.put(COLUMN_DESCRIPTION,des);
        contentValues.put(COLUMN_LEVEL,level);
        contentValues.put(COLUMN_TIME,time);
        if (develop){
            contentValues.put(COLUMN_DEVELOP,DEVELOPED);
        }else {
            contentValues.put(COLUMN_DEVELOP,DEVELOPING);
        }
        return contentValues;
    }
}
