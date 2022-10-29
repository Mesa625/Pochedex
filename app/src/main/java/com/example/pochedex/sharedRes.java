package com.example.pochedex;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class sharedRes {
    Map<String, Integer> images = new HashMap<String, Integer>();

    public sharedRes() {
        this.setImages();
    }

    public Map<String, Integer> getImages() {
        return images;
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
        pocheList = sortList(pocheList);
        return pocheList;
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
