package com.mikeapps.gasalert.UI;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mikeapps.gasalert.DBdata.Dispositivo;
import com.mikeapps.gasalert.R;

import java.util.UUID;

/**
 * Created by Classic on 29/4/2016.
 */
public class Fragmentid extends Fragment {
    EditText etidbal;
    Dispositivo dispositivo;
    Button btnmonit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.id_fragment,container,false);
        etidbal=(EditText)rootview.findViewById(R.id.ETidbal);
        btnmonit=(Button)rootview.findViewById(R.id.BTNmonit);
        btnmonit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monitorearbalanza();
            }
        });
        return rootview;
    }

    public void monitorearbalanza(){
        //nombrered=etnombrered.getText().toString();
        //clavered=etclavered.getText().toString();
        String idbal=etidbal.getText().toString();
        if(TextUtils.isEmpty(idbal)){
            etidbal.setError("Campo Necesario");
            return;
//        }else if(TextUtils.isEmpty(nombrered)){
//            etnombrered.setError("Campo Necesario");
//            return;
//        }else if(TextUtils.isEmpty(clavered)){
//            etclavered.setError("Campo Necesario");
//            return;
        }else{
            dispositivo = new Dispositivo(Integer.valueOf(idbal), "123bal", null, getPhoneName());
            dispositivo.createinDB(getActivity());
            Intent intent = new Intent(getActivity(), ConfigproveedorActivity.class);
            intent.putExtra("Edicion",false);
            startActivity(intent);
            getActivity().finish();
            //sendparamsBT(nombrered,clavered, idbal);
        }
    }

    public String getPhoneName() {
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        String deviceName = myDevice.getName();
        return deviceName;
    }
}
