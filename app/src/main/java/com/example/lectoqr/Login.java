package com.example.lectoqr;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText edtUsuario, edtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtUsuario = findViewById(R.id.et_username);
        edtPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.button_signin);
        //sesionActiva();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                finish();
                //validarUsuario("https://institucioneducativadeograciascardona.com/index.php/Android/Login/iniciarSesion");
            }
        });
    }

    private void validarUsuario(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("OK")){
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(Login.this, "Bienvenido", Toast.LENGTH_LONG).show();
                }else if(response.equalsIgnoreCase("ERROR")){
                    Toast.makeText(Login.this, "Usuario o contraseña incorrecta", Toast.LENGTH_LONG).show();
                }else if(response.equalsIgnoreCase("ERROR_NODATA")){
                    Toast.makeText(Login.this, "Por favor escribir datos", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("user", edtUsuario.getText().toString());
                parametros.put("password", edtPassword.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void sesionActiva(){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("usuario", edtUsuario.toString());
        editor.putString("contrasena", edtPassword.toString());

        editor.commit();
    }

    public boolean validarCampos (String usuario, String contrasena) {
        if (usuario.isEmpty() || contrasena.isEmpty()){
            Toast.makeText(this, "Por favor ingrese usuario y contraseña", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }
}
