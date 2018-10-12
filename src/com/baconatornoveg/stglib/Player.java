package com.baconatornoveg.stglib;

import java.util.ArrayList;

public class Player {

    private String god;
    private ArrayList<Item> build;

    public Player(String god, ArrayList<Item> build) {
        this.god = god;
        this.build = build;
    }

    public String getGod() {
        return this.god;
    }

    public String getBuild() {
        return build.toString();
    }

    public String toString() {
        return this.god + " - " + build.toString();
    }

}
