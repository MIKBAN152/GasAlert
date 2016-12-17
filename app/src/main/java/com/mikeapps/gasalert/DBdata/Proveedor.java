package com.mikeapps.gasalert.DBdata;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mikeapps.gasalert.Utils.AdminSQLiteOpenHelper;
import com.mikeapps.gasalert.Utils.utilDB;

/**
 * Created by Classic on 5/4/2016.
 */
//"'nombre_proveedor' TEXT,'celular' TEXT
public class Proveedor {
    String nombre;
    String celular;

    public Proveedor(String nombre, String celular) {
        this.nombre = nombre;
        this.celular = celular;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
    public void createinDB(Context context){
        utilDB utildb=new utilDB();
        utildb.CreateProveedorinDB(context,getNombre(),getCelular());
    }
    public void setfromDB(Context context){
        String dbname="GasAlertDB";
        String tablename="Proveedor";
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context,dbname,null,1);
        SQLiteDatabase bd= admin.getWritableDatabase();
        Cursor c = bd.rawQuery("select * from " + tablename, null);
        int fin=c.getCount();
        if(fin > 0) {
            c.moveToFirst();
            setNombre(c.getString(1));
            setCelular(c.getString(2));
            }
        else {
            Log.d("Proveedor", "no hay registro");
        }
            bd.close();
            c.close();
    }
}
