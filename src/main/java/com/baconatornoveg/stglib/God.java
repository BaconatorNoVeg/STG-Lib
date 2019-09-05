package com.baconatornoveg.stglib;

import java.util.Arrays;
import java.util.List;

public class God {

    private String name;
    private String position;
    private String[] physicals = {"Assassin", "Hunter", "Warrior"};
    private String[] magicals = {"Mage", "Guardian"};

    protected God(String name, String position) {
        this.name = name;
        this.position = position;
    }

    public boolean checkBuild(List<Item> build) {
        for (Item aBuild : build) {
            if (!aBuild.available(this)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPhysical() {
        return Arrays.asList(physicals).contains(position);
    }

    public boolean isMagical() {
        return Arrays.asList(magicals).contains(position);
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
