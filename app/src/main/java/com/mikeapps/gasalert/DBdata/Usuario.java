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
//"'calle1' text,'num_casa' TEXT, 'calle2' TEXT,'sector' TEXT, 'referencia' text
public class Usuario {
    String calle1;
    String calle2;
    String numcasa;
    String sector;
    String referencia;

    public Usuario(String calle1, String calle2, String numcasa, String sector, String referencia) {
        this.calle1 = calle1;
        this.calle2 = calle2;
        this.numcasa = numcasa;
        this.sector = sector;
        this.referencia = referencia;
    }

    public String getCalle1() {
        return calle1;
    }

    public void setCalle1(String calle1) {
        this.calle1 = calle1;
    }

    public String getCalle2() {
        return calle2;
    }

    public void setCalle2(String calle2) {
        this.calle2 = calle2;
    }

    public String getNumcasa() {
        return numcasa;
    }

    public void setNumcasa(String numcasa) {
        this.numcasa = numcasa;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public void createinDB(Context context){
        utilDB utildb=new utilDB();
        utildb.CreateUsuarioinDB(context,getCalle1(),getNumcasa(),getCalle2(),getSector(),getReferencia());
    }
    public void setfromDB(Context context){
        String dbname="GasAlertDB";
        String tablename="Usuario";
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context,dbname,null,1);
        SQLiteDatabase bd= admin.getWritableDatabase();
        Cursor c = bd.rawQuery("select * from " + tablename, null);
        int fin=c.getCount();

        if(fin > 0) {
            c.moveToFirst();
            setCalle1(c.getString(1));
            setNumcasa(c.getString(2));
            setCalle2(c.getString(3));
            setSector(c.getString(4));
            setReferencia(c.getString(5));

        }
        else {
            Log.d("Usuario", "no hay registro");
        }
        bd.close();
        c.close();
    }
}
