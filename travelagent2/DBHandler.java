package com.example.oscar.travelagent2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "APP_DATABASE.db";
    public static final String TABLE_USERS = "USERS_INFO";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_GENDER = "Gender";
    public static final String COLUMN_COUNTRY = "Country";

    public DBHandler(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_users = "CREATE TABLE "+ TABLE_USERS +
                "("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ //0號
                    COLUMN_NAME+" VARCHAR(20), "+ //1號
                    COLUMN_EMAIL+" VARCHAR(30), "+ //2號
                    COLUMN_PASSWORD+" VARCHAR(20),"+ //3號
                    COLUMN_GENDER+" VARCHAR(5),"+ //4號
                    COLUMN_COUNTRY+" VARCHAR(20)"+");"; //5號

        db.execSQL(create_table_users);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+ TABLE_USERS);
        onCreate(db);
    }
    public void addMember(Member member){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,member.get_name());
        values.put(COLUMN_EMAIL,member.get_email());
        values.put(COLUMN_PASSWORD,member.get_password());
        values.put(COLUMN_GENDER,member.get_gender());
        values.put(COLUMN_COUNTRY, member.get_country());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public void deleteMember(String name){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USERS + " WHERE " + COLUMN_NAME + " =\" " + name + " \"; ");
        db.close();
    }

    public void update_Member(String column,String info,String name){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_USERS+" SET +"+column+"+ = '"+info+"' WHERE Name = "+name+"");
        db.close();
    }

    public Member UserChecked(String account,String password){
        Member member=null;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_USERS;

        Cursor c = db.rawQuery(query,null);
        int rows_num = c.getCount();

        if(rows_num!=0){
            c.moveToFirst();
            for(int i=0;i<rows_num;i++){
                if(account.equals(c.getString(2)) && password.equals(c.getString(3))){
                    member = new Member(c.getString(1));
                    member.set_email(c.getString(2));
                    member.set_password(c.getString(3));
                    member.set_gender(c.getString(4));
                    member.set_country(c.getString(5));
                    break;
                }
                c.moveToNext();
            }
        }else{
            System.out.printf("No information!");
        }
        c.close();
        db.close();
        return member;
    }


}
