package com.example.pochedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ShowData extends AppCompatActivity implements View.OnClickListener{
    //Instance shared resources
    sharedRes shared = new sharedRes();
    //Image list
    Map<String, Integer> images = shared.getImages();
    //Sounds list
    Map<String, Integer> sounds = shared.getSounds();
    //Sound player
    MediaPlayer player;
    //Pochemon instance
    com.example.pochedex.pochemon myPochemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        //Get pochemon from intent
        Intent i = getIntent();
        String strNum = i.getStringExtra("num");
        strNum = strNum.replace("#", "0");
        int pocheNum = Integer.parseInt(strNum);
        myPochemon = getPochemon(ShowData.this, pocheNum);
        showPochemon(ShowData.this, myPochemon);

        //Set up buttons
        ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(this);
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
                        //Log.d("type", String.valueOf(attributes.get(0)));

                        //Return pokemon object
                        return new pochemon(number, name, size, weight, desc,
                                attributes, weaknesses, evolutions, images.get(name), sounds.get(name));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //In case of no coincidence a empty pochemon is returned
        return new pochemon(0, null, null, null, null,
                null, null, null, 0, 0);
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
        LinearLayout evoLayout = findViewById(R.id.evo_layout);

        //Set view contents
        title.setText(pochemon.getName());
        try {
            titleLayout.setBackgroundColor(getColor(shared.getColorFromType(String.valueOf(pochemon.getAttributes().get(0)))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        img.setImageResource(images.get(pochemon.getName()));
        img.setOnClickListener(this);
        desc.setText(pochemon.getDesc());
        size.setText(pochemon.getSize());
        weight.setText(pochemon.getWeight());
        setInnerTextLayout(context, typeLayout, pochemon.getAttributes());
        setInnerTextLayout(context, weakLayout, pochemon.getWeaknesses());
        setEvolutions(pochemon.getEvolutions());
        play();
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
            //Add the textview to the linearlayout
            layout.addView(tempCard);
        }
    }

    private void setEvolutions(JSONArray attribute){
        for (int i = 0; i < attribute.length(); i++){
            try {
               switch (i){
                   case 0:
                       ImageButton button = findViewById(R.id.evo1);
                       button.setBackgroundResource(images.get(attribute.get(i)));
                       button.setContentDescription((CharSequence) attribute.get(i));
                       button.setOnClickListener(this);
                   case 1:
                       ImageView imgView = findViewById(R.id.arrow1);
                       imgView.setBackgroundResource(R.drawable.ic_baseline_arrow_forward_ios_24);
                       button = findViewById(R.id.evo2);
                       button.setBackgroundResource(images.get(attribute.get(i)));
                       button.setContentDescription((CharSequence) attribute.get(i));
                       button.setOnClickListener(this);
                   case 2:
                       imgView = findViewById(R.id.arrow2);
                       imgView.setBackgroundResource(R.drawable.ic_baseline_arrow_forward_ios_24);
                       button = findViewById(R.id.evo3);
                       button.setBackgroundResource(images.get(attribute.get(i)));
                       button.setContentDescription((CharSequence) attribute.get(i));
                       button.setOnClickListener(this);
               }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //Back to main activity
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.poche_img:
                play();
                break;
            default:
                Intent i = new Intent(this, ShowData.class);
                String number = shared.getNumber(this, String.valueOf(view.getContentDescription()));
                i.putExtra("num", number);
                finish();
                startActivity(i);
                //Log.d("name", String.valueOf(view.getContentDescription()));
                break;
        }
    }

    //Sound handling
    public void play(){
        if (player == null){
            player = MediaPlayer.create(this, myPochemon.getSound());
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stop();
                }
            });
        }
        player.start();
    }

    public void stop(){
        if(player != null){
            player.release();
            player = null;
        }
    }

    protected void onStop() {
        super.onStop();
        stop();
    }
}