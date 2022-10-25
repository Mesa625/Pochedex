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
        images.put("Bulbasaur", R.drawable._01_bulbasaur);
        images.put("Charmander", R.drawable._04_charmander);
        images.put("Squirtle", R.drawable._07_squirtle);
    }

    private List<Integer> sortList(List<Integer> inputList){
        List<Integer> sortedList = new ArrayList<Integer>();
        for(int i = 0; i < inputList.size(); i++){

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
}
