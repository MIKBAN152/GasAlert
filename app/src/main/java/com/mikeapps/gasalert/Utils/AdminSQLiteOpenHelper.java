package com.mikeapps.gasalert.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Classic on 4/4/2016.
 */
public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table 'Dispositivo' ('localid' integer not null unique primary key autoincrement,"+
                "'id_ws' integer,'serial' TEXT, 'mac_bluetooth' TEXT,'mac_celular' TEXT)");
        db.execSQL("create table 'Usuario' ('localid' integer not null unique primary key autoincrement,"+
                "'calle1' text,'num_casa' TEXT, 'calle2' TEXT,'sector' TEXT, 'referencia' text)");
        db.execSQL("create table 'Proveedor' ('localid' integer not null unique primary key autoincrement,"+
                "'nombre_proveedor' TEXT,'celular' TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists 'Dispositivo'");
        db.execSQL("drop table if exists 'Usuario'");
        db.execSQL("drop table if exists 'Proveedor'");

        onCreate(db);
    }
}
