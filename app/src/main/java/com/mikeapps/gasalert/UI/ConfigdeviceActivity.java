package com.mikeapps.gasalert.UI;

import android.app.Activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mikeapps.gasalert.DBdata.Dispositivo;
import com.mikeapps.gasalert.R;
import com.mikeapps.gasalert.Utils.httprequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class ConfigdeviceActivity extends Activity {

    EditText etserial, etnombrered, etclavered;
    Button btnregistrarbalanza;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Dispositivo dispositivo;

    String nombrered;
    String clavered;
    int idoserial;
    Fragment fragmentserial = new Fragmentserial();
    Fragment fragmentid = new Fragmentid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configdevice);

        Intent newint = getIntent();
        address = newint.getStringExtra(BTdevicesActivity.EXTRA_ADDRESS);
        idoserial = newint.getIntExtra("regormon", 1);
        android.app.FragmentManager fm = getFragmentManager();

        if(idoserial==1){
            fm.beginTransaction()
                    .replace(R.id.contenedor,fragmentid)
                    .commit();
        }else{
            fm.beginTransaction()
                    .replace(R.id.contenedor,fragmentserial)
                    .commit();
            etserial=(EditText)findViewById(R.id.ETserial);
            etclavered=(EditText)findViewById(R.id.ETclavered);
            etnombrered=(EditText)findViewById(R.id.ETnombrered);
            btnregistrarbalanza=(Button)findViewById(R.id.btnregistrar);
            new ConnectBT().execute();
        }



    }


    public void registrarbalanza(String serialbal, String nombrered, String clavered){
        this.nombrered=nombrered;
        this.clavered=clavered;
//        String serialbal=etserial.getText().toString();
//        nombrered=etnombrered.getText().toString();
//        clavered=etclavered.getText().toString();

//        if(TextUtils.isEmpty(serialbal)){
//            etserial.setError("Campo Necesario");
//            return;
//        }else if(TextUtils.isEmpty(nombrered)){
//            etnombrered.setError("Campo Necesario");
//            return;
//        }else if(TextUtils.isEmpty(clavered)){
//            etclavered.setError("Campo Necesario");
//            return;
//        }else{
            dispositivo = new Dispositivo(0, serialbal, address, getPhoneName());
            httprequest httpreq = new httprequest();
            httpreq.Httpsetsendpost(address, getPhoneName(), serialbal, this);
       // }
    }

    public void enviado(JSONObject jsonObject){
        int localid;
        try {
            if(jsonObject.getString("message").equals("Proceso finalizado correctamente")){
                localid=jsonObject.getInt("data");
                dispositivo.setId_ws(localid);
                dispositivo.createinDB(this);
                sendparamsBT(nombrered,clavered, String.valueOf(localid));
            }
            else{
                Toast.makeText(this,"No se pudo registrar.",Toast.LENGTH_SHORT).show();}
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendparamsBT(String par1, String par2, String par3){
        msg(par1+" "+par2+" "+par3);
        String params=par1+":"+par2+":"+par3+":";
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write(params.toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }

        try {
            btSocket.close();
            Intent intent = new Intent(ConfigdeviceActivity.this, ConfigproveedorActivity.class);
            intent.putExtra("Edicion",false);
            startActivity(intent);
            finish();
        } catch (IOException e) {
            Toast.makeText(ConfigdeviceActivity.this, "Vuelva a intentarlo!!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public String getPhoneName() {
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        String deviceName = myDevice.getName();
        return deviceName;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configdevice, menu);
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
    public void msg(String s){
        Log.d("Configdev ", s);}

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(ConfigdeviceActivity.this, "Conectando...", "Por favor espere!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Conexion Fallida!");
                finish();
            }
            else
            {
                msg("Conectaddo.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            btSocket.close();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            finish();
        }
    }
}
