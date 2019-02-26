package com.baconatornoveg.stglib;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private List<Player> team = new ArrayList<>();
    private SmiteTeamGenerator stg;

    public Team(SmiteTeamGenerator stg) {
        this.stg = stg;
    }

    public void add(Player player) {
        team.add(player);
    }

    public List<String> getGods() {
        List<String> gods = new ArrayList<>();
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
            List<String> takenPositions = new ArrayList<>();
            for (Player i : team) {
                takenPositions.add(i.getGod().getPosition());
            }
            takenPositions.remove(index);
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
        StringBuilder returnString = new StringBuilder();

        returnString.append("Generated Team: \n");
        for (Player player : team) {
            returnString.append(player.getGod()).append(" - ").append(player.getBuild()).append("\n");
        }

        return returnString.toString();
    }

}
