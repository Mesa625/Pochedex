package com.example.pochedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<pochemon> myPochemon = new ArrayList<>();
    int [] pocheImg = {R.drawable._01_bulbasaur, R.drawable._04_charmander,
            R.drawable._07_squirtle};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.pocheRecyclerView);
        getMyPochemon(MainActivity.this);
        poche_RecyclerViewAdapter adapter = new poche_RecyclerViewAdapter(
                this, myPochemon);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getMyPochemon(Context context) {
        List<Integer> pocheList = new ArrayList<Integer>();
        String data = "";

        //Get captured pokemon
        InputStream inputStream = context.getResources().openRawResource(
                R.raw.poke_list);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = null;

        while(true){
            try {
                line = reader.readLine();
                if (line == null){
                    break;
                } else {
                    //Log.d("lines", line);
                    pocheList.add(Integer.parseInt(line));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Get pokemon name from number
        inputStream = context.getResources().openRawResource(
                R.raw.pokedex);
        reader = new BufferedReader(
                new InputStreamReader(inputStream));
        line = null;

        while(true){
            try {
                line = reader.readLine();
                if (line == null){
                    break;
                } else {
                    data = data+line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(!data.isEmpty()){
            try {
                //Read pokemon database
                JSONObject json = new JSONObject(data);
                JSONArray pochemon = json.getJSONArray("pochemon");
                for (int i = 0; i < pochemon.length();i++){
                    JSONObject pocheData = pochemon.getJSONObject(i);
                    //Get pokemon number
                    String strNum = pocheData.getString("num");
                    int number = Integer.parseInt(strNum);
                    //Log.d("pochemon", String.valueOf(number));
                    //If number matches to a captured pokemon
                    for (int j = 0; j < pocheList.size(); j++){
                        if(number == pocheList.get(j)){
                            //Get pokemon name to show in main
                            String name = pocheData.getString("name");
                            myPochemon.add(new pochemon(number, name,
                                    null, null, null,
                                    null, null, null,
                                    null, pocheImg[j]));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}