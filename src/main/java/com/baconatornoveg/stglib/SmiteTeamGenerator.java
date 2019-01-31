package com.baconatornoveg.stglib;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;


public class SmiteTeamGenerator {
    private final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final ArrayList<Item> BOOTS = new ArrayList<>();
    private final ArrayList<God> GODS = new ArrayList<>();
    private final ArrayList<Item> ITEMS = new ArrayList<>();

    private boolean isForcingOffensive = false;
    private boolean isForcingDefensive = false;
    public boolean isForcingBalanced = false;

    private Random rand = new Random();

    public enum Positions {
        ASSASSIN, HUNTER, MAGE, WARRIOR, GUARDIAN
    }

    public SmiteTeamGenerator() {
        // Methods to be called on creation
    }

    public String getVersion() {
        return "1.2.0";
    }

    public void getLists(boolean local) {
        Scanner in;
        InputStream bootsFile = null;
        InputStream godsFile = null;
        InputStream itemsFile = null;
        boolean useLocal = local;
        if (!local) {
            LOGGER.info("Attempting to fetch the current lists from Github...");
            try {
                String bootsRemoteUrl = "https://raw.githubusercontent.com/BaconatorNoVeg/STG-Lib/master/Lists/boots.csv";
                URL bootsUrl = new URL(bootsRemoteUrl);
                String godsRemoteUrl = "https://raw.githubusercontent.com/BaconatorNoVeg/STG-Lib/master/Lists/gods.csv";
                URL godsUrl = new URL(godsRemoteUrl);
                String itemsRemoteUrl = "https://raw.githubusercontent.com/BaconatorNoVeg/STG-Lib/master/Lists/items.csv";
                URL itemsUrl = new URL(itemsRemoteUrl);
                bootsFile = bootsUrl.openStream();
                godsFile = godsUrl.openStream();
                itemsFile = itemsUrl.openStream();
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.info("Failed to get the lists from GitHub. Falling back to compiled lists.");
                useLocal = true;
            }
        }

        if (local || useLocal){
            LOGGER.info("Using the compiled lists, these could be out of date...");
            try {
                bootsFile = getClass().getResourceAsStream("/boots.csv");
                godsFile = getClass().getResourceAsStream("/gods.csv");
                itemsFile = getClass().getResourceAsStream("/items.csv");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Begin parsing boots
        in = new Scanner(bootsFile);

        in.nextLine();

        while(in.hasNextLine()) {
            String line = in.nextLine();
            String[] values = line.split(",");
            BOOTS.add(new Item(values[0], values[1], values[2], values[3]));
        }
        in.close();
        in = new Scanner(godsFile);
        in.nextLine();

        while(in.hasNextLine()) {
            String line = in.nextLine();
            String[] values = line.split(",");
            GODS.add(new God(values[0], values[1]));
        }
        in.close();
        in = new Scanner(itemsFile);
        in.nextLine();

        while(in.hasNextLine()) {
            String line = in.nextLine();
            String[] values = line.split(",");
            ITEMS.add(new Item(values[0], values[1], values[2], values[3]));
        }
        in.close();
        LOGGER.info(BOOTS.toString() + "\n" + GODS.toString() + "\n" + ITEMS.toString());
        LOGGER.info("Smite Player Generator successfully loaded " + BOOTS.size() + " boots, " + GODS.size() + " gods, and " + ITEMS.size() + " items.");
    }

    // Implementing Fisher–Yates shuffle
    private static void shufflePositions(Positions[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Positions a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public Team generateTeam(int size, boolean forceOffensive, boolean forceDefensive, boolean forceBalanced) {
        isForcingOffensive = forceOffensive;
        isForcingDefensive = forceDefensive;
        isForcingBalanced = forceBalanced;
        Team team = new Team(this);
        Positions[] positions = Positions.values();
        if (forceBalanced) {
            // Generate a team that does not duplicate positions
            shufflePositions(positions);
            for (int i = 0; i < size; i++) {
                Player loadout = makeLoadout(positions[i]);
                team.add(loadout);
            }

        } else {
            // Generate a team that can have more than one god of the same position
            for (int i = 0; i < size; i++) {
                Player loadout = makeLoadout(positions[(int)(Math.random() * (positions.length))]);
                team.add(loadout);
            }
            boolean dupes = true;
            while(dupes) {
                dupes = false;
                for (int i = 0; i < size; i++) {
                    Player currentPlayer = team.getPlayer(i);
                    for (int j = 0; j < size; j++) {
                        if (i != j) {
                            if (currentPlayer.getGod().equals(team.getPlayer(j).getGod())) {
                                dupes = true;
                                team.set(j, makeLoadout(positions[(int)(Math.random() * (positions.length))]));
                                break;
                            }
                        }
                    }
                    if (dupes) {
                        break;
                    }
                }
            }
        }

        return team;
    }

    public Player makeLoadout(Positions position) {
        God player = null;
        ArrayList<Item> playerBuild = null;
        ArrayList<Item> build;

        switch (position) {

            case MAGE:
                player = getGod("Mage");
                build = generateBuild("mage", "magical", false);
                if (isForcingOffensive) {
                    while (true) {
                        int offensiveCount = 0;
                        for (Item i : build) {
                            if (i.isOffensive()) {
                                offensiveCount++;
                            }
                        }
                        if (offensiveCount < 5) {
                            build = generateBuild("mage", "magical", false);
                        } else {
                            break;
                        }
                    }
                }
                playerBuild = build;
                break;

            case GUARDIAN:
                player = getGod("Guardian");
                build = generateBuild("guardian", "magical", false);
                if (isForcingDefensive) {
                    while (true) {
                        int defensiveCount = 0;
                        for (Item i : build) {
                            if (i.isDefensive()) {
                                defensiveCount++;
                            }
                        }
                        if (defensiveCount < 5) {
                            build = generateBuild("guardian", "magical", false);
                        } else {
                            break;
                        }
                    }
                }
                playerBuild = build;
                break;

            case WARRIOR:
                player = getGod("Warrior");
                build = generateBuild("warrior", "physical", false);
                if (isForcingOffensive) {
                    while (true) {
                        int offensiveCount = 0;
                        for (Item i : build) {
                            if (i.isOffensive()) {
                                offensiveCount++;
                            }
                        }
                        if (offensiveCount < 4) {
                            build = generateBuild("warrior", "physical", false);
                        } else {
                            break;
                        }
                    }
                }
                playerBuild = build;
                break;

            case ASSASSIN:
                player = getGod("Assassin");
                build = generateBuild("assassin", "physical", (player.getName().equals("Ratatoskr")));
                if (isForcingOffensive) {
                    while (true) {
                        int offensiveCount = 0;
                        for (Item i : build) {
                            if (i.isOffensive()) {
                                offensiveCount++;
                            }
                        }
                        if (offensiveCount < 5) {
                            build = generateBuild("assassin", "physical", (player.getName().equals("Ratatoskr")));
                        } else {
                            break;
                        }
                    }
                }
                playerBuild = build;
                break;

            case HUNTER:
                player = getGod("Hunter");
                build = generateBuild("hunter", "physical", false);
                if (isForcingOffensive) {
                    while (true) {
                        int offensiveCount = 0;
                        for (Item i : build) {
                            if (i.toString().equals("Hastened Katana") || i.toString().equals("Masamune") || i.toString().equals("Stone Cutting Sword") || i.toString().equals("Golden Blade")) {
                                build = generateBuild("hunter", "physical", false);
                                break;
                            } else if (i.isOffensive()) {
                                offensiveCount++;
                            }
                        }
                        if (offensiveCount < 5) {
                            build = generateBuild("hunter", "physical", false);
                        } else {
                            break;
                        }
                    }
                }
                playerBuild = build;
                break;
        }

        return new Player(player, playerBuild);
    }

    private ArrayList<Item> generateBuild(String god, String type, boolean isRatatoskr) {
        ArrayList<Item> build = new ArrayList<>();
        LinkedHashSet<Item> generation = new LinkedHashSet<>();
        Item newItem;
        if (type.equals("physical")) {
            switch (god.toLowerCase()) {
                case "assassin":
                case "hunter":
                    if (isRatatoskr) {
                        generation.add(new Item("Acorn of Yggdrasil", "true", "false", "OFFENSE"));
                    } else {
                        generation.add(getPhysicalBoot(isForcingOffensive));
                    }
                    break;
                case "warrior":
                    generation.add(getPhysicalBoot(false));
                    break;
            }
            for (int i = 0; i < 5; i++) {
                newItem = getItem("physical");
                generation.add(newItem);
            }

            while (generation.size() < 6) {
                newItem = getItem("physical");
                generation.add(newItem);
            }

            build.addAll(0, generation);
        } else {
            switch (god) {
                case "mage":
                    generation.add(getMagicalBoot(isForcingOffensive, false));
                    break;
                case "guardian":
                    generation.add(getMagicalBoot(false, isForcingDefensive));
                    break;
            }
            for (int i = 0; i < 5; i++) {
                newItem = getItem("magical");
                generation.add(newItem);
            }

            while (generation.size() < 6) {
                newItem = getItem("magical");
                generation.add(newItem);
            }
            build.addAll(0, generation);
        }
        return build;
    }

    private Item getPhysicalBoot(boolean isOffensive) {
        Item boot;
        boot = BOOTS.get(rand.nextInt(BOOTS.size()));
        if (isOffensive) {
            while (boot.isMagical() || !boot.isOffensive()) {
                boot = BOOTS.get(rand.nextInt(BOOTS.size()));
            }
        } else {
            while (boot.isMagical()) {
                boot = BOOTS.get(rand.nextInt(BOOTS.size()));
            }
        }
        return boot;
    }

    private Item getMagicalBoot(boolean isOffensive, boolean isDefensive) {
        Item boot;
        boot = BOOTS.get(rand.nextInt(BOOTS.size()));
        if (isOffensive) {
            while (boot.isPhysical() || !boot.isOffensive()) {
                boot = BOOTS.get((rand.nextInt(BOOTS.size())));
            }
        } else if (isDefensive) {
            while (boot.isPhysical() || !boot.isDefensive()) {
                boot = BOOTS.get(rand.nextInt(BOOTS.size()));
            }
        } else {
            while (boot.isPhysical()) {
                boot = BOOTS.get(rand.nextInt(BOOTS.size()));
            }
        }
        return boot;
    }

    private Item getItem(String type) {
        Item item;
        Boolean physical = type.toLowerCase().equals("physical");
        item = ITEMS.get((int) (Math.random() * (ITEMS.size() - 1) + 1));
        if (physical) {
            while (item.isMagical()) {
                item = ITEMS.get((int) (Math.random() * (ITEMS.size() - 1) + 1));
            }
        } else {
            while (item.isPhysical()) {
                item = ITEMS.get((int) (Math.random() * (ITEMS.size() - 1) + 1));
            }
        }
        return item;
    }

    private God getGod(String position) {
        God god = GODS.get((int)(Math.random() * (GODS.size())));
        while (!god.getPosition().toLowerCase().equals(position.toLowerCase())) {
            god = GODS.get((int)(Math.random() * (GODS.size())));
        }
        return god;
    }

    public ArrayList<Item> getBOOTS() {
        return BOOTS;
    }

    public ArrayList<Item> getITEMS() {
        return ITEMS;
    }

    public ArrayList<God> getGODS() {
        return GODS;
    }
}