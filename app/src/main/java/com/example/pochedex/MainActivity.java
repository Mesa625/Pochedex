package com.example.pochedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<pochemon> myPochemon = new ArrayList<>();
    int [] pocheImg = {R.drawable._01_bulbasaur, R.drawable._04_charmander, R.drawable._07_squirtle};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.pocheRecyclerView);
        getMyPochemon();
        poche_RecyclerViewAdapter adapter = new poche_RecyclerViewAdapter(this, myPochemon);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getMyPochemon() {
        int[] pocheList = {1,4,7};
        String[] pocheNames = {"Bulbasaur", "Charmander", "Squirtle"};

        for (int i = 0; i < pocheList.length; i++) {
            myPochemon.add(new pochemon(pocheList[i], pocheNames[i], null, null, null,
                            null, null, null, null, pocheImg[i]));
        }
    }
}