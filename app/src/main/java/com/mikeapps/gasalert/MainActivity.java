package com.mikeapps.gasalert;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mikeapps.gasalert.UI.BTdevicesActivity;
import com.mikeapps.gasalert.UI.ConfigdeviceActivity;
import com.mikeapps.gasalert.UI.ConfigproveedorActivity;
import com.mikeapps.gasalert.UI.EstadogasActivity;
import com.mikeapps.gasalert.Utils.httprequest;
import com.mikeapps.gasalert.Utils.utilDB;

import java.io.File;
import java.net.InetAddress;
import java.util.Timer;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static java.lang.Thread.sleep;

public class MainActivity extends Activity {


    private httprequest httpmreq;

    public View rootview;
    public Button btnmonit, btnreg;
    WifiManager wifi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getActionBar().hide();
        btnmonit=(Button)findViewById(R.id.btnmonit);
        btnreg=(Button)findViewById(R.id.btnreg);
        rootview=getWindow().getDecorView().getRootView();
        btnmonit.setVisibility(View.GONE);
        btnreg.setVisibility(View.GONE);
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);


        if(hasInternetConnectivity(this)) {
        CountDownTimer timer = new CountDownTimer(2000,2000){
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            startapp(rootview);
        }
    };
    timer.start();
//                    //startapp(rootview);
//            Thread splashThread = new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        int waited = 0;
//                        while (waited < 2000) {
//                            sleep(500);
//                            if (this.isAlive()) {
//                                waited += 500;
//                            }
//                        }
//                    } catch (InterruptedException e) {
//                        // do nothing
//                    } finally {
//                        startapp(rootview);
//                        // start your activity here using startActivity
//
//                    }
//                }
//            };
//            splashThread.start();
        }else{
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Necesita tener conexión a internet para poder usar la aplicación.");
            dlgAlert.setTitle(getString(R.string.app_name) + " - Sin Conexión");
            dlgAlert.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Intent intent = new Intent(this, HomeActivity.class);
                    finish();
                }
            });
            dlgAlert.setNegativeButton("Prender WiFi?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    wifi.setWifiEnabled(true);
                    int contador = 0;
                    while (contador < 10) {
                        contador++;
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {

                        }
                        if (hasInternetConnectivity(MainActivity.this)) {
                            break;
                        } else if (contador == 9) {
                            Toast.makeText(MainActivity.this, "No se pudo conectar al internet", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    startapp(rootview);
                    //startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
                    //finish();
                }
            });
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        }

    }

        public void startapp(View view) {
        utilDB utildb=new utilDB();
        if(utildb.dbexists(this)){
            Intent intent = new Intent(MainActivity.this, EstadogasActivity.class);
            startActivity(intent);
            finish();
        }else {
            btnreg.setVisibility(view.VISIBLE);
            btnmonit.setVisibility(view.VISIBLE);
        }
    }

    public void gotoMonitorear(View view){
        Intent intent = new Intent(MainActivity.this, ConfigdeviceActivity.class);
        startActivity(intent);
        intent.putExtra("regormon", 1);
        finish();
    }
    public void gotoRegistrar(View view){
        Intent intent = new Intent(MainActivity.this, BTdevicesActivity.class);
        intent.putExtra("regormon", 2);
        startActivity(intent);
        finish();
    }
//    public boolean userexists(Context context, String dbName) {
//        File dbFile = context.getDatabasePath(dbName);
//        return dbFile.exists();
//    }

    public static boolean hasInternetConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
