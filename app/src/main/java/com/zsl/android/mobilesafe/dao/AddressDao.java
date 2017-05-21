package com.zsl.android.mobilesafe.dao;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {
    /**
     * @param context The context use to getFilesDir()
     * @param number  Phone number, must be the beginning 7 numbers of a cell phone number. Because address.db only have cell phone numbers.
     * @return Address if number is in DB, or ""
     */
    public static String getAddress(Context context, String number) {
        String result = "";
        if (number.matches("^\\d{7}$")) {
            String path = context.getFilesDir() + "/address.db";
            SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = database.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)", new String[]{number});
            if (cursor.moveToNext()) {
                result = cursor.getString(0);
            }
            database.close();
        }
        return result;
    }
}
