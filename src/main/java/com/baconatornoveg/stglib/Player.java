package com.baconatornoveg.stglib;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private God god;
    private List<Item> build;

    public Player(God god, List<Item> build) {
        this.god = god;
        this.build = build;
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

    public List<Item> getBuildAsItems() {
        return build;
    }

    public String toString() {
        return this.god + " - " + build.toString();
    }

}
