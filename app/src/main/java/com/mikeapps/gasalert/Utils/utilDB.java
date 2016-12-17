package com.mikeapps.gasalert.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.sql.SQLDataException;

/**
 * Created by Classic on 4/4/2016.
 */
public class utilDB {
    Context context;
    public void setContext(Context c){
        context=c;
    }
    private void _(String s){
        Log.d("DBprocess", "###" + s);
    }

    public void CreateDispotivoinDB(Context context,int id_ws,String serial, String macbt,String devname){
        String dbname="GasAlertDB";
        String tablename="Dispositivo";
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context,dbname,null,1);
        SQLiteDatabase bd= admin.getWritableDatabase();
//id_ws' int,'serial' TEXT, 'mac_bluetooth' TEXT,'mac_celular'
        bd.execSQL("delete from " + tablename);
        ContentValues registro = new ContentValues();
        registro.put("id_ws",id_ws);
        registro.put("serial", serial);
        registro.put("mac_bluetooth",macbt);
        registro.put("mac_celular",devname);
        bd.insert(tablename, null, registro);
        bd.close();
        return;
    }


    public void CreateUsuarioinDB(Context context,String calle1,String numcasa, String calle2,
                                  String sector, String referencia){
        String dbname="GasAlertDB";
        String tablename="Usuario";
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context,dbname,null,1);
        SQLiteDatabase bd= admin.getWritableDatabase();
//"'calle1' text,'num_casa' TEXT, 'calle2' TEXT,'sector' TEXT, 'referencia' text
        bd.execSQL("delete from " + tablename);
        ContentValues registro = new ContentValues();
        registro.put("calle1",calle1);
        registro.put("num_casa", numcasa);
        registro.put("calle2",calle2);
        registro.put("sector", sector);
        registro.put("referencia", referencia);
        bd.insert(tablename, null, registro);
        bd.close();
        return;
    }

    public void CreateProveedorinDB(Context context,String npro, String celpro){
        String dbname="GasAlertDB";
        String tablename="Proveedor";
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context,dbname,null,1);
        SQLiteDatabase bd= admin.getWritableDatabase();
//"'nombre_proveedor' TEXT,'celular' TEXT
        bd.execSQL("delete from " + tablename);
        ContentValues registro = new ContentValues();
        registro.put("nombre_proveedor",npro);
        registro.put("celular", celpro);
        bd.insert(tablename, null, registro);
        bd.close();
        return;
    }

    public boolean dbexists(Context context){
        String dbName="GasAlertDB";
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}
