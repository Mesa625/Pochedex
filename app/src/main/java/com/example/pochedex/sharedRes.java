package com.example.pochedex;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class sharedRes {
    Map<String, Integer> images = new HashMap<String, Integer>();
    Map<String, Integer> sounds = new HashMap<String, Integer>();

    private static final String FILE_NAME = "poke_list.dat";

    public sharedRes() {
        this.setImages(); this.setSounds();
    }

    public Map<String, Integer> getImages() {
        return images;
    }
    public Map<String, Integer> getSounds() {
        return sounds;
    }

    //Set image list as a "dictionary"
    private void setImages() {
        images.put("Bulbasaur", R.drawable._1_bulbasaur);
        images.put("Ivysaur", R.drawable._2_ivysaur);
        images.put("Venusaur", R.drawable._3_venusaur);
        images.put("Charmander", R.drawable._4_charmander);
        images.put("Charmeleon", R.drawable._5_charmeleon);
        images.put("Charizard", R.drawable._6_charizard);
        images.put("Squirtle", R.drawable._7_squirtle);
        images.put("Wartortle", R.drawable._8_wartortle);
        images.put("Blastoise", R.drawable._9_blastoise);
        images.put("Pichu", R.drawable._172_pichu);
        images.put("Pikachu", R.drawable._25_pikachu);
        images.put("Raichu", R.drawable._26_raichu);
        images.put("Ditto", R.drawable._132_ditto);
        images.put("Mewtwo", R.drawable._150_mewtwo);
        images.put("Mew", R.drawable._151_mew);
    }

    //Set sound list as a "dictionary"
    private void setSounds() {
        sounds.put("Bulbasaur", R.raw._01_bulbasaur);
        sounds.put("Ivysaur", R.raw._02_ivysaur);
        sounds.put("Venusaur", R.raw._03_venusaur);
        sounds.put("Charmander", R.raw._04_charmander);
        sounds.put("Charmeleon", R.raw._05_charmeleon);
        sounds.put("Charizard", R.raw._06_charizard);
        sounds.put("Squirtle", R.raw._07_squirtle);
        sounds.put("Wartortle", R.raw._08_wartortle);
        sounds.put("Blastoise", R.raw._09_blastoise);
        sounds.put("Pichu", R.raw._172_pichu);
        sounds.put("Pikachu", R.raw._25_pikachu);
        sounds.put("Raichu", R.raw._26_raichu);
        sounds.put("Ditto", R.raw._132_ditto);
        sounds.put("Mewtwo", R.raw._150_mewtwo);
        sounds.put("Mew", R.raw._151_mew);
    }

    //Sort pochemon numbers from smallest to largest
    private List<Integer> sortList(List<Integer> inputList){
        //Get input list
        List<Integer> sortedList = inputList;

        //Iterate through list
        for(int i = 0; i < sortedList.size(); i++){
            int min_index = i;
            //Get position of smallest value so far
            for(int j = i+1; j < sortedList.size(); j++){
                if (sortedList.get(j) < sortedList.get(min_index)){
                    min_index = j;
                }
            }
            //Swap value in i with smallest value
            int temp = sortedList.get(i);
            sortedList.set(i, sortedList.get(min_index));
            sortedList.set(min_index, temp);
        }
        return sortedList;
    }

    public List<Integer> getPocheList(Context context){
        //Get captured pokemon
        List<Integer> pocheList = new ArrayList<Integer>();
        try {
            FileInputStream fileInputStream = context.openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(fileInputStream));
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
            pocheList = sortList(pocheList);
            return pocheList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return pocheList;
        }
    }

    public String getNumber(Context context, String name){
        String data = getPochedex(context);

        if (!data.isEmpty()) {
            try {
                //Read pokemon database
                JSONObject json = new JSONObject(data);
                JSONArray pochemon = json.getJSONArray("pochemon");
                for (int i = 0; i < pochemon.length(); i++) {
                    JSONObject pocheData = pochemon.getJSONObject(i);

                    //Get pokemon name
                    String pocheName = pocheData.getString("name");

                    if(pocheName.equals(name)){
                        //Get pokemon number from name
                        String strNum = pocheData.getString("num");

                        //Return pokemon number
                        return strNum;
                    }
                }
            } catch(JSONException e){
                e.printStackTrace();
            }
        }

        return null;
    }

    public String getPochedex(Context context){
        //Get pokemon name from number
        InputStream inputStream = context.getResources().openRawResource(
                R.raw.pokedex);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String data = "";

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
        return data;
    }

    public int getColorFromType(String type){
        if(type.equals("Grass")){
            return R.color.grass;
        } else if(type.equals("Fire")){
            return R.color.fire;
        } else if(type.equals("Water")){
            return R.color.water;
        } else if(type.equals("Electric")){
            return R.color.electric;
        } else if(type.equals("Psychic")){
            return R.color.psychic;
        } else if(type.equals("Flying")){
            return R.color.flying;
        } else if(type.equals("Ice")){
            return R.color.ice;
        } else if(type.equals("Poison")){
            return R.color.poison;
        } else if(type.equals("Rock")){
            return R.color.rock;
        } else if(type.equals("Ground")){
            return R.color.ground;
        } else if(type.equals("Fighting")){
            return R.color.fighting;
        } else if(type.equals("Ghost")){
            return R.color.ghost;
        } else if(type.equals("Dark")){
            return R.color.dark;
        } else if(type.equals("Bug")){
            return R.color.bug;
        } else {
            return R.color.normal;
        }
    }
}
