package com.mikeapps.gasalert.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.mikeapps.gasalert.UI.ConfigdeviceActivity;
import com.mikeapps.gasalert.UI.ConfigproveedorActivity;
import com.mikeapps.gasalert.UI.EstadogasActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by Classic on 4/4/2016.
 */
public class httprequest extends Activity {
    public final String apiURL = "http://104.131.71.204/" +
            "medicion-gas/ws_api/public/api/v1/";
    //User URL
    public final String registroURL=apiURL + "registro";//POST
    public final String setmedicionURL=apiURL + "medicion";//POST
    public final String getmedicionURL=apiURL + "mediciones/";//GET 0=idbalanza
    private OkHttpClient client=new OkHttpClient();

    Context context=null;
    public String Httpsetsendpost(String btmac, String devname, String serial, Context c){

        context=c;
        HashMap<String, String>parameters=new HashMap<>();
        parameters.put("mac_balanza", btmac);
        parameters.put("mac_celular",devname);
        parameters.put("serial_balanza",serial);
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,String> param : parameters.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            try {
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

            String urllocal=registroURL;
            Httpsendpost Hsp = new Httpsendpost();
            Hsp.execute(urllocal, postData.toString());
            Log.d("step", postData.toString());//print the string that is being used

        //excute the post sending the url of the web service and the parsed data to post in one strin


        return null;
    }

    //this is the complementing part of the last function
    //here the connection is made and the posting of the data too
    public class Httpsendpost extends AsyncTask<String, String, JSONObject> {

        ProgressDialog pDialog;
        public final MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        JSONObject responsejob=new JSONObject();
        private String resptxt;
        Response response=null;
        //public final MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //progress message building c is the context that is passed in the function setActivity
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Registrando");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {


            if (params[0] != null && params[1] != null) {
               // _("postdoin");
                RequestBody body = RequestBody.create(mediaType, params[1]);

                Request request = new Request.Builder()
                        .url(params[0])
                        .post(body)
                        .build();

                try {
                    response = client.newCall(request).execute();
                    Log.d("response", response.toString());

                    if (response.code() == 200) {
                         resptxt = response.body().string();
                        responsejob = readStream(resptxt);
                     }
                    Log.d("response",responsejob.toString());


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //httpprogress=true;
            //Jsonresponse = responsejob;
            return responsejob;

            //return response;//returning the json object with all the data in the response
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            pDialog.dismiss();
            ((ConfigdeviceActivity)context).enviado(jsonObject);
        }
    }

    public JSONObject readStream(String jsontxt) throws IOException {
        //_("stream");
        JSONObject jobj=null;
        InputStream stream = new ByteArrayInputStream(jsontxt.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(stream),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line+"\n");
            //Log.d("response", sb.toString());
        }
        //Log.d("response","right here");
        stream.close();
        String json=sb.toString();
        //Log.d("stream", json);
        try {
            jobj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jobj;
    }


    public void Httpgetlevel(Context context, int id_disp){
        this.context=context;
        Httpget httpget = new Httpget();
        httpget.execute(getmedicionURL, String.valueOf(id_disp));
        return;
    }

    public class Httpget extends AsyncTask<String, String, JSONObject>{
        JSONObject responsejobj;
        String response;
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Obteniendo Estado");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            if(params[0] !=null && params[1] !=null) {
                URL url = null;
                try {
                    url = new URL(params[0]+params[1]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                //Log.d("http", url.toString());
                _(params[0]+params[1]);
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    //_(response.toString());
                    //_(response.headers().toString());

                    responsejobj=readStream(response.body().source().readUtf8());
                    _(responsejobj.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return responsejobj;
        }
        @Override
        protected void onPostExecute(JSONObject s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            if(s!=null) {
                pDialog.dismiss();

                    ((EstadogasActivity)context).processgaslevel(s);
            }else{
                Toast.makeText(context,"No se pudo obtener valor",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void _(String s){
        Log.d("HTTPCLASS", "##" + s);
    }
    int ii=0;
    public void Httpgetconnected(Context context, int id_disp){
        this.context=context;
        Httpgetcon httpgetcon = new Httpgetcon();
        httpgetcon.execute(getmedicionURL, String.valueOf(id_disp));
        return;
    }
    ProgressDialog pDialogcon;

    public class Httpgetcon extends AsyncTask<String, String, Boolean>{
        JSONObject responsejobj;
        String response;
//        ProgressDialog pDialogcon;
        Boolean CONNECTION_STATE=false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogcon = new ProgressDialog(context);
            pDialogcon.setTitle("Verificando conexión...");
            pDialogcon.setMessage("Por favor espere mientras la balanza se conecta al servidor.");
            pDialogcon.setIndeterminate(false);
            pDialogcon.setCancelable(false);
            pDialogcon.show();
        }
        @Override
        protected Boolean doInBackground(String... params) {
            if(params[0] !=null && params[1] !=null) {
                URL url = null;
                try {
                    url = new URL(params[0]+params[1]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                //Log.d("http", url.toString());
                _(params[0] + params[1]);
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                for (int i=1;i<=200;i++){
//                    final Handler handler = new Handler(Looper.getMainLooper());
//                    final Runnable task = new Runnable() {
//                        int k=1;
//                        @Override
//                        public void run() {
//                            k++;
//                            _("try " + k);
//                            handler.postDelayed(this, 2000);
//                        }
//                    };
//                    handler.post(task);

                    try {
                        Response response = client.newCall(request).execute();
                        //_(response.toString());
                        //_(response.headers().toString());

                        responsejobj=readStream(response.body().source().readUtf8());
                        _(responsejobj.toString());
                        if(responsejobj.getString("message").equals("Solicitud exitosa")){
                            CONNECTION_STATE=true;
                            ii=100;
                            runOnUiThread(changeMessage);
                            break;
                        }else{
                            ii=i/2;
                            runOnUiThread(changeMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }
//                    if (i>=20){
//                        handler.removeCallbacks();
//                    }
                }

            }
            return CONNECTION_STATE;
        }
        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            pDialogcon.dismiss();
            if(s!=null) {
                ((ConfigproveedorActivity)context).processgetconnection(s);
            }else{
                Toast.makeText(context,"Ocurrió un error en la conexión",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Runnable changeMessage = new Runnable() {
        @Override
        public void run() {
            CountDownTimer timer = new CountDownTimer(2000,2000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                pDialogcon.setMessage("No coloque el tanque todavía... \nProgreso: " + ii + "%");
            }
        };
        timer.start();

        }
    };
}
