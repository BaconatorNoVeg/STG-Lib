package com.baconatornoveg.stglib;

import java.util.ArrayList;

public class Team {

    ArrayList<Player> team = new ArrayList<>();

    public Team() {

    }

    public void add(Player player) {
        team.add(player);
    }

    public ArrayList<String> getGods() {
        ArrayList<String> gods = new ArrayList<>();
        for (Player i : team) {
            gods.add(i.getGod());
        }
        return gods;
    }

}
