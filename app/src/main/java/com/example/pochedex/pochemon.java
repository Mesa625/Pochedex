package com.example.pochedex;

public class pochemon {
    private int number;
    private String name;
    private String size;
    private String weight;
    private String desc;
    private String[] attributes;
    private String[] weaknesses;
    private String[] evolutions;
    private String[] involutions;
    public int image;

    public pochemon(int number, String name, String size, String weight, String desc,
                    String[] attributes, String[] weaknesses,
                    String[] evolutions, String[] involutions, int image) {
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

    public String[] getAttributes() {
        return attributes;
    }

    public String[] getWeaknesses() {
        return weaknesses;
    }

    public String[] getEvolutions() {
        return evolutions;
    }

    public String[] getInvolutions() {
        return involutions;
    }

    public int getImage() {
        return image;
    }
}
