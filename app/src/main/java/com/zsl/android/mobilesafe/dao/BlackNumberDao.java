package com.zsl.android.mobilesafe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/5/30.
 */

public class BlackNumberDao {

    private final Context mContext;
    private final BlackNumberHelper mBlackNumberHelper;

    BlackNumberDao(Context context) {
        mContext = context;
        mBlackNumberHelper = new BlackNumberHelper(mContext);
    }

    public void add(String number, int mode){
        SQLiteDatabase db = mBlackNumberHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode", mode);
        db.insert("blacknumber", null, values);
        db.close();
    }

    class BlackNumberHelper extends SQLiteOpenHelper {
        public BlackNumberHelper(Context context) {
            super(context, "blacknumber.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table blacknumber(_id integer primary key autoincrement, number char(11), mode integer)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
