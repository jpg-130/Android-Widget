package com.example.getstarted;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuoteReaderWriter extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Quotes.db";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_VAL = "quote";
    public static final String TABLE_NAME = "Quote";

// DONE: Methods for storing quotes in db when online and extract quotes from db.
    public QuoteReaderWriter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        making a database
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME +
                "(" +COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_VAL+ " VARCHAR); ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertQuoteInDb(String newQuote){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_VAL, newQuote );
        long success = db.insert(TABLE_NAME, null, contentValues);
        if(success!=-1)
            return true;
        return false;
    }

    public String fetchQuote(){
        String[] arrData;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"*"}, null, null,
                null, null, null);
        if(cursor!=null){
            //fetch first quote
            if(cursor.moveToFirst()){
                arrData = new String[cursor.getColumnCount()];
                arrData[0]=cursor.getString(0); //Column_ID
                arrData[1]=cursor.getString(1); //Quote
                //Delete Fetched quote
                db.delete(TABLE_NAME, COLUMN_ID+" = "+arrData[0], null);
                //returning fetched quote
                return arrData[1];
            }
            cursor.close();
        }
        //else return null
        return null;
    }
}
