package com.baconatornoveg.stglib;

import java.util.ArrayList;

public class Player {

    private God god;
    private ArrayList<Item> build;

    public Player(God god, ArrayList<Item> build) {
        this.god = god;
        this.build = build;
    }

    public God getGod() {
        return this.god;
    }

    public ArrayList<String> getBuild() {
        ArrayList<String> buildList = new ArrayList<>();
        for (Item aBuild : build) buildList.add(aBuild.toString());
        return buildList;
    }

    public String toString() {
        return this.god + " - " + build.toString();
    }

}
