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

    public void set(int index, Player player) {
        team.set(index, player);
    }

    public int getSize() {
        return team.size();
    }

    public Player getPlayer(int index) {
        return team.get(index);
    }

    public String toString() {
        String returnString = "";

        returnString += "Generated Team: \n";
        for (int i = 0; i < team.size(); i++) {
            returnString += team.get(i).getGod() + " - " + team.get(i).getBuild() + "\n";
        }

        return returnString;
    }

}
