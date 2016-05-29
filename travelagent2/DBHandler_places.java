package com.example.oscar.travelagent2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;


public class DBHandler_places extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "APP_DATABASE2.db";
    public static final String TABLE_PLACES = "USERS_PLACES";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_PLACE = "Place";


    public DBHandler_places(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_place ="CREATE TABLE "+TABLE_PLACES +
                "("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    COLUMN_NAME+" VARCHAR(20), "+//1號
                    COLUMN_PLACE+" VARCHAR(30)"+");"; //2號

        db.execSQL(create_table_place);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+ TABLE_PLACES);
        onCreate(db);
    }
    public boolean addPlace(Member member){
        boolean pass = true;
        if(same_place(member.get_name(), member.get_place())){ //找到一樣的地方
            pass = false;
        }else{
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME,member.get_name());
            values.put(COLUMN_PLACE,member.get_place());
            SQLiteDatabase db = getWritableDatabase();
            db.insert(TABLE_PLACES, null, values);
            db.close();
        }
        return pass;
    }
    public boolean deletePlace(String name,String place){
        boolean pass = true;
        if(!same_place(name,place)){ //沒找到一樣的地方
            pass = false;
        }else{
            SQLiteDatabase db = getWritableDatabase();
            String query = "DELETE FROM "+TABLE_PLACES+ " WHERE Name LIKE '"+name+"' AND Place LIKE '"+place+"'";
            db.execSQL(query);
            db.close();
        }

        return pass;
    }
    public ArrayList<String> getLocation(String name){
        ArrayList<String> arrayList = null;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_PLACES+" WHERE Name LIKE '"+name+"'";
        Cursor c = db.rawQuery(query, null);
        int rows_num = c.getCount();
        if(rows_num!=0) {
            arrayList= new ArrayList<>();
            c.moveToFirst();
            for(int i=0;i<rows_num;i++){
                arrayList.add(c.getString(2));
                c.moveToNext();
            }
        }
        c.close();
        db.close();
        return arrayList;
    }
    private boolean same_place(String name,String location){
        boolean pass = false;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_PLACES+" WHERE Name LIKE '"+name+"'";
        Cursor c = db.rawQuery(query, null);
        int rows_num = c.getCount();
        if(rows_num!=0) {
            c.moveToFirst();
            for(int i=0;i<rows_num;i++){
                if(location.equals(c.getString(2))){
                    pass = true;
                    break;
                }
                c.moveToNext();
            }
        }
        c.close();
        db.close();

        return pass;
    }
}
