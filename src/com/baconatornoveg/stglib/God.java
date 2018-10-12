package com.baconatornoveg.stglib;

public class God {

    private String name;
    private String position;

    protected God(String name, String position) {
        this.name = name;
        this.position = position;
    }

    public String getName() {
        return this.name;
    }

    public String getPosition() {
        return this.position;
    }

    public String toString() {
        return this.name;
    }

}
