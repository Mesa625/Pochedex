package com.example.pochedex;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton scanButton = findViewById(R.id.PokeballButton);
        scanButton.setOnClickListener(view -> openQR_Scan());
    }

    public void openQR_Scan(){
        Intent scan_activity = new Intent(this, QR_Scan.class);
        startActivity(scan_activity);
    }
}