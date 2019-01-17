package com.baconatornoveg.stglib;

import java.util.ArrayList;

public class Team {

    ArrayList<Player> team = new ArrayList<>();
    SmiteTeamGenerator stg;

    public Team(SmiteTeamGenerator stg) {
        this.stg = stg;
    }

    public void add(Player player) {
        team.add(player);
    }

    public ArrayList<String> getGods() {
        ArrayList<String> gods = new ArrayList<>();
        for (Player i : team) {
            gods.add(i.getGod().getName());
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

    public void rerollPlayer(int index) {
        Player selectedPlayer = team.get(index);
        Player rerollingPlayer;
        SmiteTeamGenerator.Positions[] positions = SmiteTeamGenerator.Positions.values();
        if (!stg.isForcingBalanced) {
            ArrayList<God> taken = new ArrayList<>();
            for (Player i : team) {
                taken.add(i.getGod());
            }
            rerollingPlayer = stg.makeLoadout(positions[(int)(Math.random() * (positions.length))]);
            boolean testing = true;
            while (testing) {
                testing = false;
                for (God takenGod : taken) {
                    if (rerollingPlayer.getGod().equals(takenGod) || rerollingPlayer.getGod().equals(selectedPlayer.getGod())) {
                        rerollingPlayer = stg.makeLoadout(positions[(int) (Math.random() * (positions.length))]);
                        testing = true;
                        break;
                    }
                }
            }
            set(index, rerollingPlayer);
        } else {
            ArrayList<String> takenPositions = new ArrayList<>();
            for (Player i : team) {
                takenPositions.add(i.getGod().getPosition());
            }
            rerollingPlayer = stg.makeLoadout(positions[(int)(Math.random() * (positions.length))]);
            boolean testing = true;
            while (testing) {
                testing = false;
                for (String takenPosition : takenPositions) {
                    if (rerollingPlayer.getGod().getPosition().equals(takenPosition)) {
                        rerollingPlayer = stg.makeLoadout(positions[(int) (Math.random() * (positions.length))]);
                        testing = true;
                        break;
                    }
                }
            }
            set(index, rerollingPlayer);
        }
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
