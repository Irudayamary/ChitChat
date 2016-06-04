package com.usimedia.chitchat.model.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.usimedia.chitchat.model.ChatContacts;
import com.usimedia.chitchat.model.Column;
import java.text.SimpleDateFormat;

/**
 * Created by USI IT on 6/4/2016.
 */
public class Localcontacts extends SQLiteOpenHelper {

    private static final String DATABASENAME = "ChitChat";
    private static final String TABLENAME = "Contacts";
    private static final int VERSION = 1;
    private static final Column ID;
    private static final Column EMAIL;
    private static final Column NAME;
    private static final Column LASTSEEN;
    private static final Column STATUS;
    private static final String SPACE = " ";

    static {
        ID = new Column ("_id","INTEGER PRIMARY KEY AUTOINCREMENT");
        EMAIL = new Column ("email","TEXT");
        NAME = new Column ("name","TEXT");
        LASTSEEN = new Column ("lastseen","TEXT");
        STATUS = new Column("status", "TEXT");
    }

    private void createtable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS"
                .concat(SPACE).concat(TABLENAME).concat(SPACE)
                .concat("(").concat(SPACE).concat(ID.getName()).concat(SPACE).concat(ID.getType())
                .concat(",").concat(SPACE).concat(EMAIL.getName()).concat(SPACE).concat(EMAIL.getType())
                .concat(",").concat(SPACE).concat(NAME.getName()).concat(SPACE).concat(NAME.getType())
                .concat(",").concat(SPACE).concat(LASTSEEN.getName()).concat(SPACE).concat(LASTSEEN.getType())
                .concat(",").concat(SPACE).concat(STATUS.getName()).concat(SPACE).concat(STATUS.getType()).concat(")").concat(";"));

    }

    private void droptable(SQLiteDatabase db){
        db.execSQL("DROP IF EXISTS"
                .concat("(")
                .concat(SPACE).concat(TABLENAME).concat(SPACE)
                .concat(")").concat(";"));

    }

    private void insertchatcontact(ChatContacts contacts){
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        cv.put(EMAIL.getName(),contacts.getEmail());
        cv.put(NAME.getName(),contacts.getName());
        cv.put(LASTSEEN.getName(),dateFormat.format(contacts.getLastSeen()));
        cv.put(STATUS.getName(),contacts.getStatus());
        db.insert(TABLENAME, null, cv);

        }

    private Cursor getAllContacts()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT" + "*" + "FROM" + TABLENAME + ";" ;
        return  db.rawQuery(query , null);
    }

    public Localcontacts(Context context) {
        super(context, DATABASENAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createtable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        droptable(sqLiteDatabase);
        createtable(sqLiteDatabase);
    }


}
