package com.yojulab.yojucounter.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ohsanghun on 10/29/16.
 */

public class OpenHelper extends SQLiteOpenHelper implements ConstantsImpl{
    public OpenHelper(Context c){
        super(c, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_T_COUNTER_INFOR);
        db.execSQL(CREATE_T_DAILY_COUNT);
        db.execSQL(CREATE_T_COUNT_LOG);
    }

    public void onDrop(SQLiteDatabase db){
        db.execSQL(DROP_T + T_COUNTER_INFORMATION + " ;");
        db.execSQL(DROP_T + T_DAILY_COUNT + " ;");
        db.execSQL(DROP_T + T_COUNT_LOG + " ;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("debug", "Version mismatch : "+oldVersion + "to "+newVersion);
//        onDrop(db);
        onCreate(db);
    }
}