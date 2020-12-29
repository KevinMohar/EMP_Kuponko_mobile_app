package com.example.test2.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.test2.Database.RacuniContract.*;
import com.example.test2.Database.TrgovineContract.*;
import com.example.test2.Database.MesecContract.*;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "racunko.db";
    public static final int DATABASE_VERSION = 1;



    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // tabela za racune
        final String SQL_CREATE_RACUNI_TABLE = "CREATE TABLE " +
                RacunEntry.TABLE_NAME + " (" + RacunEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RacunEntry.COLUM_1_NAME + " INTEGER NOT NULL, " + RacunEntry.COLUM_2_DATUM +
                " TEXT NOT NULL, " + RacunEntry.COLUM_3_ZNESEK +
                " REAL NOT NULL" + ");";

        // tabela za trgovine
        final String SQL_CREATE_TRGOVINE_TABLE = "CREATE TABLE " + TrgovinaEntry.TABLE_NAME +
                " (" + TrgovinaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TrgovinaEntry.COLUM_1_NAME
                + " TEXT NOT NULL, " + TrgovinaEntry.COLUM_2_NASLOV + " TEXT NOT NULL" + ");";

        // tabela za mesece
        final String SQL_CREATE_MESCI_TABLE = "CREATE TABLE " + MesecEntry.TABLE_NAME +
                " (" + MesecEntry.COLUM_1_DATUM + " TEXT NOT NULL, " + MesecEntry.COLUM_2_STROSKI
                + " REAL NOT NULL, " + MesecEntry.COLUM_2_RACUNI + " TEXT NOT NULL" + ");";

        db.execSQL(SQL_CREATE_RACUNI_TABLE);
        db.execSQL(SQL_CREATE_TRGOVINE_TABLE);
        db.execSQL(SQL_CREATE_MESCI_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
