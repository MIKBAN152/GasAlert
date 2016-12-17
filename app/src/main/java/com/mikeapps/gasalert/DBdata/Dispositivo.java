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
public class Dispositivo {
    int id_ws;
    String serial;
    String mac_bt;
    String mac_cel;

    public Dispositivo(int id_ws, String serial, String mac_bt, String mac_cel){
        setId_ws(id_ws);
        setMac_bt(mac_bt);
        setMac_cel(mac_cel);
        setSerial(serial);
        Log.d("DCREAdo", String.valueOf(id_ws));
    }

    public int getId_ws() {
        return id_ws;
    }

    public void setId_ws(int id_ws) {
        this.id_ws = id_ws;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getMac_bt() {
        return mac_bt;
    }

    public void setMac_bt(String mac_bt) {
        this.mac_bt = mac_bt;
    }

    public String getMac_cel() {
        return mac_cel;
    }

    public void setMac_cel(String mac_cel) {
        this.mac_cel = mac_cel;
    }

    public void createinDB(Context context){
        utilDB utildb=new utilDB();
        utildb.CreateDispotivoinDB(context, getId_ws(),getSerial(),getMac_bt(),getMac_cel());
    }

    public void setfromDB(Context context){
        String dbname="GasAlertDB";
        String tablename="Dispositivo";
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context,"GasAlertDB",null,1);
        SQLiteDatabase bd= admin.getWritableDatabase();
        Cursor c = bd.rawQuery("select * from Dispositivo", null);
        int fin=c.getCount();

        if(fin > 0) {
            c.moveToFirst();
            setId_ws(c.getInt(1));
            setSerial(c.getString(2));
            setMac_bt(c.getString(3));
            setMac_cel(c.getString(4));
        }
        else {
            Log.d(tablename, "no hay registro");
        }
        bd.close();
        c.close();
    }
}
