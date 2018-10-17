package com.baconatornoveg.stglib;

import java.util.ArrayList;

public class Player {

    private String god;
    private ArrayList<Item> build;

    Player(String god, ArrayList<Item> build) {
        this.god = god;
        this.build = build;
    }

    String getGod() {
        return this.god;
    }

    ArrayList<String> getBuild() {
        ArrayList<String> buildList = new ArrayList<>();
        for (Item aBuild : build) buildList.add(aBuild.toString());
        return buildList;
    }

    public String toString() {
        return this.god + " - " + build.toString();
    }

}
