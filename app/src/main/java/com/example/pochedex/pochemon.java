package com.example.pochedex;

import org.json.JSONArray;

public class pochemon {
    private int number;
    private String name;
    private String size;
    private String weight;
    private String desc;
    private JSONArray attributes;
    private JSONArray weaknesses;
    private JSONArray evolutions;
    public int image;
    public int sound;

    public pochemon(int number, String name, String size, String weight, String desc,
                    JSONArray attributes, JSONArray weaknesses,
                    JSONArray evolutions, int image, int sound) {
        this.number = number;
        this.name = name;
        this.size = size;
        this.weight = weight;
        this.desc = desc;
        this.attributes = attributes;
        this.weaknesses = weaknesses;
        this.evolutions = evolutions;
        this.image = image;
        this.sound = sound;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getWeight() {
        return weight;
    }

    public String getDesc() {
        return desc;
    }

    public JSONArray getAttributes() {
        return attributes;
    }

    public JSONArray getWeaknesses() {
        return weaknesses;
    }

    public JSONArray getEvolutions() {
        return evolutions;
    }

    public int getImage() {
        return image;
    }

    public int getSound() {
        return sound;
    }
}
