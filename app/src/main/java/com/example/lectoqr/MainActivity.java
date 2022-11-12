package com.example.lectoqr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
  //https://script.google.com/macros/s/AKfycbxutK8lKkg2S2Fn7uNCwMPhfV0dJbSBmYmqbclIqCyGXRbo7gHmJyLjfbaIwSvFm4jF/exec
  //AKfycbxutK8lKkg2S2Fn7uNCwMPhfV0dJbSBmYmqbclIqCyGXRbo7gHmJyLjfbaIwSvFm4jF

  String scannedData;
  Button scanBtn, watchBtn;

  String nombre, apellido, telefono, placa;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final Activity activity = this;
    scanBtn = findViewById(R.id.scanBtn);
    watchBtn = findViewById(R.id.watchBtn);
    ImageView Imagen = findViewById(R.id.qrcode);

    ArrayList<String> datosUsuarios = new ArrayList<>();


    nombre = "Dimitri";
    apellido = "Perez";
    telefono = "315";
    placa = "PFD474";

    datosUsuarios.add(nombre);
    datosUsuarios.add(apellido);
    datosUsuarios.add(telefono);
    datosUsuarios.add(placa);

    scanBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Por favor ubique el código QR en el centro del recuadro.");
        integrator.setBeepEnabled(true);
        integrator.setCameraId(0);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
      }
    });

    watchBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //Intent intent = new Intent(MainActivity.this, Listado.class);
        //startActivity(intent);
        try {
          BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
          Bitmap bitmap = barcodeEncoder.encodeBitmap(datosUsuarios.toString(), BarcodeFormat.QR_CODE, 750, 750);
          Imagen.setImageBitmap(bitmap);
        }catch (Exception e){
          e.printStackTrace();
        }
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode, data);
    if (result!=null){
      scannedData = result.getContents();
      if (scannedData!=null){
        //here be need to handle scanned data...
        new SendRequest().execute();
      }else{

      }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  public class SendRequest extends AsyncTask<String, Void, String> {


    protected void onPreExecute(){}

    protected String doInBackground(String... arg0) {

      try{

        //Enter script URL Here
        URL url = new URL("https://script.google.com/macros/s/AKfycbzREcj2oXgZY06a_Pp51t_hEnFsTwmpZDhMWnTZ9lFext5hEbjpCSDrpb-MU7-mtt8w/exec");

        JSONObject postDataParams = new JSONObject();

        //int i;
        //for(i=1;i<=70;i++)


        //    String usn = Integer.toString(i);

        //Passing scanned code as parameter

        postDataParams.put("sdata",scannedData);


        Log.e("params",postDataParams.toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(postDataParams));

        writer.flush();
        writer.close();
        os.close();

        int responseCode=conn.getResponseCode();

        if (responseCode == HttpsURLConnection.HTTP_OK) {

          BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
          StringBuffer sb = new StringBuffer("");
          String line="";

          while((line = in.readLine()) != null) {

            sb.append(line);
            break;
          }

          in.close();
          return sb.toString();

        }
        else {
          return new String("false : "+responseCode);
        }
      }
      catch(Exception e){
        return new String("Exception: " + e.getMessage());
      }
    }

    @Override
    protected void onPostExecute(String result) {
      //Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
      Toast.makeText(getApplicationContext(), "Escaneado",Toast.LENGTH_LONG).show();

    }
  }

  public String getPostDataString(JSONObject params) throws Exception {

    StringBuilder result = new StringBuilder();
    boolean first = true;

    Iterator<String> itr = params.keys();

    while(itr.hasNext()){

      String key= itr.next();
      Object value = params.get(key);

      if (first)
        first = false;
      else
        result.append("&");

      result.append(URLEncoder.encode(key, "UTF-8"));
      result.append("=");
      result.append(URLEncoder.encode(value.toString(), "UTF-8"));

    }
    return result.toString();
  }
}