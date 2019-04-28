package com.baconatornoveg.stglib;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

    public String[] getBuildAsStringArray() {
        String[] buildArray = new String[6];
        for (int i = 0; i < 6; i++) {
            buildArray[i] = build.get(i).toString();
        }
        return buildArray;
    }
    
    public String[] getRelicsAsStringArray() {
        String[] relicsArray = new String[2];
        for (int i = 0; i < 2; i++) {
            relicsArray[i] = relics.get(i).toString();
        }
        return relicsArray;
    }

    public List<Item> getBuildAsItems() {
        return build;
    }
    public List<Item> getRelicsAsItems() { return relics; }

    public String toJSON() {
        JSONObject returnObj = new JSONObject();
        JSONArray jsonBuild = new JSONArray();
        for (Item i : build) {
            jsonBuild.add(i.toString());
        }
        JSONArray jsonRelics = new JSONArray();
        for (Item i : relics) {
            jsonRelics.add(i.toString());
        }
        returnObj.put("god", god.getName());
        returnObj.put("build", jsonBuild);
        returnObj.put("relics", jsonRelics);
        return returnObj.toJSONString();
    }

    public String toString() {
        return this.god + " - " + build.toString() + " - Relics: " + relics.toString();
    }

}
