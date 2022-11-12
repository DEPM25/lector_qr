package com.example.lectoqr;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.util.Log;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Listado extends AppCompatActivity {


    ListView listView;
    ListAdapter adapter;
    ProgressDialog loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);

        listView = findViewById(R.id.lv_items);

        getItems();

    }

    String URL = "https://script.google.com/macros/s/AKfycbzYAXRxX0OtkpLKQJDELOt1hmCx4XSlSSdsSKZ_xYpSbL1qRidig64jhK6HBbQvBH2n/exec";


    private void getItems() {

        loading =  ProgressDialog.show(this,"Cargando","Por favor, espere",false,true);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseItems(response);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        int socketTimeOut = 50000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }


    private void parseItems(String jsonResposnce) {

        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        try {
            JSONObject jobj = new JSONObject(jsonResposnce);
            JSONArray jarray = jobj.getJSONArray("user");

            //String date = new SimpleDateFormat("d/M/yyyy").format(new Date());
            //Log.i("Fecha hoy", date);


            for (int i = 0; i < jarray.length(); i++) {

                JSONObject jo = jarray.getJSONObject(i);

                String itemName = jo.getString("Grado-Grupo");
                String name = jo.getString("Nombres");
                String lastname = jo.getString("Apellidos");
                String fecha_entrada = jo.getString("Fecha-A");

                //String anio2 = "";
                //anio2 = fecha_entrada.split("a")[0];

               //Log.i("fecha", anio2);


                HashMap<String, String> item = new HashMap<>();
                item.put("Grado-Grupo", itemName);
                item.put("Nombres", name);
                item.put("Apellidos", lastname);
                item.put("Fecha-A", fecha_entrada);

                //if(fecha_entrada.equalsIgnoreCase("10/9/2021a")){
                    list.add(item);
                //}
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        adapter = new SimpleAdapter(this,list,R.layout.list_item_row,
                new String[]{"Grado-Grupo","Nombres","Apellidos", "Fecha-A"},new int[]{R.id.tv_item_name,R.id.tv_brand,R.id.tv_price,R.id.tv_fecha});


        listView.setAdapter(adapter);
        loading.dismiss();
    }
}