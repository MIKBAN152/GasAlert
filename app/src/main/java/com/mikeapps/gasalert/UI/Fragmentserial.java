package com.mikeapps.gasalert.UI;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mikeapps.gasalert.R;

/**
 * Created by Classic on 29/4/2016.
 */
public class Fragmentserial extends Fragment {
    Button btnserial;
    EditText etserial, etnombrered, etclavered;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        final View rootview =  inflater.inflate(R.layout.serial_fragment,container,false);
        btnserial=(Button)rootview.findViewById(R.id.BTNserial);
        etserial=(EditText)rootview.findViewById(R.id.ETserial);
        etnombrered=(EditText)rootview.findViewById(R.id.ETnombrered);
        etclavered=(EditText)rootview.findViewById(R.id.ETclavered);
        btnserial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serialbal = etserial.getText().toString();
                String nombrered = etnombrered.getText().toString();
                String clavered = etclavered.getText().toString();
                if(TextUtils.isEmpty(serialbal)){
                    etserial.setError("Campo Necesario");
                    return;
                }else if(TextUtils.isEmpty(nombrered)){
                    etnombrered.setError("Campo Necesario");
                    return;
                }else if(TextUtils.isEmpty(clavered)){
                    etclavered.setError("Campo Necesario");
                    return;
                }else {
                    ((ConfigdeviceActivity) getActivity()).registrarbalanza(serialbal, nombrered, clavered);
                }
            }
        });
        return rootview;
    }
}
