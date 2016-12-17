package com.mikeapps.gasalert.UI;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mikeapps.gasalert.DBdata.Dispositivo;
import com.mikeapps.gasalert.DBdata.Proveedor;
import com.mikeapps.gasalert.DBdata.Usuario;
import com.mikeapps.gasalert.MainActivity;
import com.mikeapps.gasalert.R;
import com.mikeapps.gasalert.Utils.AdminSQLiteOpenHelper;
import com.mikeapps.gasalert.Utils.httprequest;

public class ConfigproveedorActivity extends Activity {

    EditText etnombreproveedor, etcelularpro, etcalle1, etcalle2, etnumcasa, etreferencia, etsector;
    String nompro,celpro,calle1,calle2,numcasa,referencia,sector;
    Button btnregproveedor;
    httprequest httpreq=new httprequest();
    Usuario usuario;
    Proveedor proveedor;
    Dispositivo dispositivo;
    boolean EDIT_STATE=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent =getIntent();
        Bundle bundle=intent.getExtras();
//        String macbt=bundle.getString("macbt");
//        String devname=bundle.getString("devname");
//        String serial=bundle.getString("serial");
        setContentView(R.layout.activity_configproveedor);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        etnombreproveedor=(EditText)findViewById(R.id.ETnombrepro);
        etcelularpro=(EditText)findViewById(R.id.ETfonopro);
        etcalle1=(EditText)findViewById(R.id.ETcalle1);
        etcalle2=(EditText)findViewById(R.id.ETcalle2);
        etnumcasa=(EditText)findViewById(R.id.ETnumerocasa);
        etreferencia=(EditText)findViewById(R.id.ETreferencia);
        etsector=(EditText)findViewById(R.id.ETsector);
        dispositivo=new Dispositivo(0,null,null,null);
        dispositivo.setfromDB(this);
        EDIT_STATE=bundle.getBoolean("Edicion");
        if (EDIT_STATE){
            Usuario usuario = new Usuario(null,null,null,null,null);
            usuario.setfromDB(this);
            Proveedor proveedor = new Proveedor(null,null);
            proveedor.setfromDB(this);
            etnombreproveedor.setText(proveedor.getNombre());
            etcelularpro.setText(proveedor.getCelular());
            etcalle1.setText(usuario.getCalle1());
            etcalle2.setText(usuario.getCalle2());
            etnumcasa.setText(usuario.getNumcasa());
            etreferencia.setText(usuario.getReferencia());
            etsector.setText(usuario.getSector());
        }
    }

    public void registrodatosenvio(View view){
        nompro=etnombreproveedor.getText().toString();
        celpro=etcelularpro.getText().toString();
        calle1=etcalle1.getText().toString();
        calle2=etcalle2.getText().toString();
        numcasa=etnumcasa.getText().toString();
        sector=etsector.getText().toString();
        referencia=etreferencia.getText().toString();
        if(TextUtils.isEmpty(nompro)){
            etnombreproveedor.setError("Campo Necesario");
            return;
        }else if(TextUtils.isEmpty(celpro)){
            etcelularpro.setError("Campo Necesario");
            return;
        }if(TextUtils.isEmpty(calle1)){
            etcalle1.setError("Campo Necesario");
            return;
        }if(TextUtils.isEmpty(calle2)){
            etcalle2.setError("Campo Necesario");
            return;
        }if(TextUtils.isEmpty(numcasa)){
            etnumcasa.setError("Campo Necesario");
            return;
        }if(TextUtils.isEmpty(sector)){
            etsector.setError("Campo Necesario");
            return;
        }if(TextUtils.isEmpty(referencia)){
            etreferencia.setError("Campo Necesario");
            return;
        }else {
            usuario = new Usuario(calle1,calle2,numcasa,sector,referencia);
            usuario.createinDB(this);
            proveedor = new Proveedor(nompro,celpro);
            proveedor.createinDB(this);

            if(EDIT_STATE){
                finish();
            }else{
//                Intent intent=new Intent(ConfigproveedorActivity.this, EstadogasActivity.class);
//                startActivity(intent);
//                finish();
                httprequest httpreq=new httprequest();
                httpreq.Httpgetconnected(this, dispositivo.getId_ws());
            }
        }
    }

    public void processgetconnection(Boolean CON_STATE){
        if (CON_STATE){
            Toast.makeText(ConfigproveedorActivity.this,
                    "La balanza está lista, ahora puede colocar el tanque.", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(ConfigproveedorActivity.this, EstadogasActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(ConfigproveedorActivity.this,
                    "La balanza no pudo conectarse, por favor configúrela nuevamente.",
                    Toast.LENGTH_LONG).show();
            resetfunc();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configproveedor, menu);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(EDIT_STATE){
            finish();
        }else{
            resetfunc();
        }
    }

    public void resetfunc(){
        String dbname="GasAlertDB";
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, dbname, null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        this.deleteDatabase(dbname);
        bd.close();
        Intent intent=new Intent(ConfigproveedorActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
