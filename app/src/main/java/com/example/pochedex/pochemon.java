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
    private JSONArray involutions;
    public int image;

    public pochemon(int number, String name, String size, String weight, String desc,
                    JSONArray attributes, JSONArray weaknesses,
                    JSONArray evolutions, JSONArray involutions, int image) {
        this.number = number;
        this.name = name;
        this.size = size;
        this.weight = weight;
        this.desc = desc;
        this.attributes = attributes;
        this.weaknesses = weaknesses;
        this.evolutions = evolutions;
        this.involutions = involutions;
        this.image = image;
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

    public JSONArray getInvolutions() {
        return involutions;
    }

    public int getImage() {
        return image;
    }
}
