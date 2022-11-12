package com.example.lectoqr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

  ImageView logo;
  TextView nombre;
  Animation fronnombre, fromlogo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.activity_splash_screen);

    logo = findViewById(R.id.viewLogo);
    nombre = findViewById(R.id.view_nombre);

    fromlogo = AnimationUtils.loadAnimation(this,R.anim.fromlogo);
    fronnombre = AnimationUtils.loadAnimation(this,R.anim.fromnombre);

    logo.setAnimation(fromlogo);
    nombre.setAnimation(fronnombre);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        Intent i = new Intent(SplashScreen.this, Login.class);
        startActivity(i);
        finish();
      }
    }, 1800);
  }
}