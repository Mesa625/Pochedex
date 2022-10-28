package com.example.pochedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //Pokemon list
    ArrayList<pochemon> myPochemon = new ArrayList<>();
    //Instance shared resources
    sharedRes shared = new sharedRes();
    //Image list
    Map<String, Integer> images = shared.getImages();

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
        RecyclerView recyclerView = findViewById(R.id.pocheRecyclerView);
        getMyPochemon(MainActivity.this);
        poche_RecyclerViewAdapter adapter = new poche_RecyclerViewAdapter(
                this, myPochemon);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getMyPochemon(Context context) {
        //Get captured pochemon
        List<Integer> pocheList = shared.getPocheList(context);

        //Get pochedex json file
        String data = shared.getPochedex(context);

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

                            //Get pokemon attributes
                            JSONArray attributes = pocheData.getJSONArray("attrib");
                            //Log.d("type", String.valueOf(attributes.get(0)));

                            //Add pokemon to recycleView list
                            myPochemon.add(new pochemon(number, name,
                                    null, null, null,
                                    attributes, null, null,
                                    null, images.get(name)));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}