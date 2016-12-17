package com.mikeapps.gasalert.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikeapps.gasalert.DBdata.Dispositivo;
import com.mikeapps.gasalert.DBdata.Proveedor;
import com.mikeapps.gasalert.DBdata.Usuario;
import com.mikeapps.gasalert.MainActivity;
import com.mikeapps.gasalert.R;
import com.mikeapps.gasalert.Services.MyService;
import com.mikeapps.gasalert.Utils.AdminSQLiteOpenHelper;
import com.mikeapps.gasalert.Utils.httprequest;

import org.json.JSONException;
import org.json.JSONObject;

public class EstadogasActivity extends Activity {

    ImageView gasiv, btniv;
    TextView porctv, mnstv, fhtv, idbaltv;
    Boolean ESTADO_BTN=false;
    Usuario usuario;
    Proveedor proveedor;
    Dispositivo dispositivo;
    int nivelgas=0;
    MyService myservice= new MyService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadogas);
        gasiv=(ImageView)findViewById(R.id.IVgas);
        btniv=(ImageView)findViewById(R.id.IVbtn);
        porctv=(TextView)findViewById(R.id.TVporcentaje);
        mnstv=(TextView)findViewById(R.id.TVbutfun);
        fhtv=(TextView)findViewById(R.id.TVfecha);
        usuario=new Usuario(null,null,null,null,null);
        proveedor=new Proveedor(null,null);
        dispositivo=new Dispositivo(0,null,null,null);
        usuario.setfromDB(this);
        proveedor.setfromDB(this);
        dispositivo.setfromDB(this);
        idbaltv=(TextView)findViewById(R.id.TVidbal);
        dispositivo.setfromDB(this);
        idbaltv.setText(" # " + String.valueOf(dispositivo.getId_ws()));

    }

    public void funcbtn(View view){

        if(!ESTADO_BTN){
            getgaslevel(this);
        }else{
            String pedido=getString(R.string.bodysms);
            String bodysms=proveedor.getNombre()+" " + pedido +" " +usuario.getCalle1()+" No. "+usuario.getNumcasa()+" y "+usuario.getCalle2()
                    +", Sector:"+usuario.getSector()+", "+usuario.getReferencia();
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(proveedor.getCelular(), null, bodysms, null, null);
            _(proveedor.getCelular());
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.setData(Uri.parse("sms:"+proveedor.getCelular()));
            smsIntent.putExtra("sms_body",bodysms);
            startActivity(smsIntent);
        }
    }

    public void getgaslevel(Context context){

        httprequest httpreq=new httprequest();
        httpreq.Httpgetlevel(context, dispositivo.getId_ws());
    }
    public void processgaslevel(JSONObject json){
        String fechahora=null;
        try {
            if(json.getString("message").equals("Solicitud exitosa")){
                nivelgas=json.getJSONObject("data").getInt("porcentaje");
                fechahora=json.getJSONObject("data").getString("created_at");
            String j=String.valueOf(nivelgas);
            _(j);
                updateview(j, fechahora);
            }else{

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateview(String j, String fh){
        porctv.setText(j+"%");
        fhtv.setText(fh);
        if(nivelgas<=15){
            gasiv.setImageDrawable(getResources().getDrawable(R.drawable.tanque50));
            btniv.setImageDrawable(getResources().getDrawable(R.drawable.btnpedirgas));
            mnstv.setText(getString(R.string.enviar_texto));
            ESTADO_BTN=true;
            //comentar esta linea si no funciona el servicio
            //this.myservice.immediateAlert();
        } else{
            gasiv.setImageDrawable(getResources().getDrawable(R.drawable.tanque));
            btniv.setImageDrawable(getResources().getDrawable(R.drawable.btnupdate));
            mnstv.setText(getString(R.string.updategastxt));
            ESTADO_BTN=false;
        }
    }
    private void _(String s){
        Log.d("EstadoGAS", "" + s);
    }

    public void resetfunc(View view){

        String dbname="GasAlertDB";
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, dbname, null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        this.deleteDatabase(dbname);
        bd.close();
        Intent intent=new Intent(EstadogasActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
    @Override
    protected void onResume() {
        super.onResume();
        getgaslevel(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_estadogas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reset) {
            resetfunc(getWindow().getDecorView().getRootView());
            return true;
        }
        if (id == R.id.action_proveedor) {
            showproveedor(this);
            return true;
        }
        if (id == R.id.action_misdatos) {
            showmisdatos(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void showproveedor(Context context){
        Proveedor proveedor = new Proveedor(null,null);
        proveedor.setfromDB(context);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Datos Proveedor");
        alert.setCancelable(true);
        alert.setMessage("Nombre: " + proveedor.getNombre() + "\n" +
                "Teléfono: " + proveedor.getCelular());
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(EstadogasActivity.this, ConfigproveedorActivity.class);
                intent.putExtra("Edicion", true);
                startActivity(intent);
                //finish();
            }
        });
        alert.show();
//        Toast.makeText(EstadogasActivity.this, , Toast.LENGTH_SHORT).show();
    }
    void showmisdatos(Context context){
        Usuario usuario = new Usuario(null, null, null, null, null);
        usuario.setfromDB(context);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Dirección de Entrega");
        alert.setCancelable(true);
        alert.setMessage("Calle principal: " + usuario.getCalle1() + "\n" +
                "Calle secundaria: " + usuario.getCalle2() + "\n" +
                "#: " + usuario.getNumcasa() + "\n" +
                "Referencia: " + usuario.getReferencia() + "\n" +
                "Sector: " + usuario.getSector());
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(EstadogasActivity.this, ConfigproveedorActivity.class);
                intent.putExtra("Edicion", true);
                startActivity(intent);
                //finish();
            }
        });
        alert.show();
    }
}
