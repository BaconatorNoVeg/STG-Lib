package com.baconatornoveg.stglib;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private God god;
    private List<Item> build;
    private List<Item> relics;

    public Player(God god, List<Item> build, List<Item> relics) {
        this.god = god;
        this.build = build;
        this.relics = relics;
    }

    public God getGod() {
        return this.god;
    }

    public List<String> getBuild() {
        List<String> buildList = new ArrayList<>();
        for (Item aBuild : build) {
            buildList.add(aBuild.toString());
        }
        return buildList;
    }

    public List<String> getRelics() {
        List<String> relicsList = new ArrayList<>();
        for (Item relic : relics) {
            relicsList.add(relic.toString());
        }
        return relicsList;
    }

    public List<Item> getBuildAsItems() {
        return build;
    }
    public List<Item> getRelicsAsItems() { return relics; }

    public String toString() {
        return this.god + " - " + build.toString() + " - Relics: " + relics.toString();
    }

}
