package com.example.pochedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ShowData extends AppCompatActivity {
    //Instance shared resources
    sharedRes shared = new sharedRes();
    //Image list
    Map<String, Integer> images = shared.getImages();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        Intent i = getIntent();
        String strNum = i.getStringExtra("num");
        strNum = strNum.replace("#", "0");
        int pocheNum = Integer.parseInt(strNum);
        com.example.pochedex.pochemon myPochemon = getPochemon(ShowData.this, pocheNum);
        showPochemon(ShowData.this, myPochemon);
    }

    private pochemon getPochemon(Context context, int poche_number) {
        //Get pochedex json file
        String data = shared.getPochedex(context);

        if (!data.isEmpty()) {
            try {
                //Read pokemon database
                JSONObject json = new JSONObject(data);
                JSONArray pochemon = json.getJSONArray("pochemon");
                for (int i = 0; i < pochemon.length(); i++) {
                    JSONObject pocheData = pochemon.getJSONObject(i);

                    //Get pokemon number
                    String strNum = pocheData.getString("num");
                    int number = Integer.parseInt(strNum);

                    if(number == poche_number){
                        //Get pokemon attributes
                        String name = pocheData.getString("name");
                        String size = pocheData.getString("size");
                        String weight = pocheData.getString("weight");
                        String desc = pocheData.getString("description");
                        JSONArray attributes = pocheData.getJSONArray("attrib");
                        JSONArray weaknesses = pocheData.getJSONArray("weak");
                        JSONArray evolutions = pocheData.getJSONArray("evo");
                        JSONArray involutions = pocheData.getJSONArray("invo");
                        //Log.d("type", String.valueOf(attributes.get(0)));

                        //Return pokemon object
                        return new pochemon(number, name, size, weight, desc,
                                attributes, weaknesses, evolutions,
                                involutions, images.get(name));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //In case of no coincidence a empty pochemon is returned
        return new pochemon(0, null, null, null, null,
                null, null, null, null, 0);
    }

    private void showPochemon(Context context, pochemon pochemon){
        //Get views from layout
        TextView title = findViewById(R.id.poche_name);
        ConstraintLayout titleLayout = findViewById(R.id.title_layout);
        ImageView img = findViewById(R.id.poche_img);
        TextView desc = findViewById(R.id.desc);
        TextView size = findViewById(R.id.height);
        TextView weight = findViewById(R.id.weight);
        LinearLayout typeLayout = findViewById(R.id.type_layout);
        LinearLayout weakLayout = findViewById(R.id.weak_layout);

        //Set view contents
        title.setText(pochemon.getName());
        try {
            titleLayout.setBackgroundColor(getColor(shared.getColorFromType(String.valueOf(pochemon.getAttributes().get(0)))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        img.setImageResource(images.get(pochemon.getName()));
        desc.setText(pochemon.getDesc());
        size.setText(pochemon.getSize());
        weight.setText(pochemon.getWeight());
        setInnerTextLayout(context, typeLayout, pochemon.getAttributes());
        setInnerTextLayout(context, weakLayout, pochemon.getWeaknesses());
    }

    private void setInnerTextLayout(Context context, LinearLayout layout, JSONArray attribute){
        for (int i = 0; i < attribute.length(); i++){
            //New text view
            TextView temp = new TextView(context);
            CardView tempCard = new CardView(context);
            try {
                //Set text, size, text color and background color
                temp.setText(String.valueOf(attribute.get(i)));
                temp.setTextSize(24);
                temp.setTextColor(getColor(R.color.black));
                temp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                temp.setBackgroundColor(getColor(shared.getColorFromType(String.valueOf(temp.getText()))));
                tempCard.addView(temp);
                tempCard.setRadius(10);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // add the textview to the linearlayout
            layout.addView(tempCard);
        }
    }
}