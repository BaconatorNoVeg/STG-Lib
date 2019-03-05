package com.baconatornoveg.stglib;

import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class SmiteTeamGenerator {

    private final List<Item> BOOTS = new ArrayList<>();
    private final List<God> GODS = new ArrayList<>();
    private final List<Item> ITEMS = new ArrayList<>();

    public boolean isForcingOffensive = false;
    public boolean isForcingDefensive = false;
    public boolean isForcingBalanced = false;
    public boolean isForcingBoots = true;

    private Random rand = new Random();

    public enum Positions {
        ASSASSIN, HUNTER, MAGE, WARRIOR, GUARDIAN
    }

    public String getVersion() {
        return "1.3.0";
    }

    public void getLists(boolean local) {
        Scanner in;
        InputStream bootsFile = null;
        InputStream godsFile = null;
        InputStream itemsFile = null;
        boolean useLocal = local;
        if (!local) {
            System.out.println("Attempting to fetch the current lists from Github...");
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
                System.out.println("Failed to get the lists from GitHub. Falling back to compiled lists.");
                useLocal = true;
            }
        }

        if (local || useLocal) {
            System.out.println("Using the compiled lists, these could be out of date...");
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

        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[] values = line.split(",");
            BOOTS.add(new Item(values[0], values[1], values[2], values[3]));
        }
        in.close();
        in = new Scanner(godsFile);
        in.nextLine();

        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[] values = line.split(",");
            GODS.add(new God(values[0], values[1]));
        }
        in.close();
        in = new Scanner(itemsFile);
        in.nextLine();

        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[] values = line.split(",");
            ITEMS.add(new Item(values[0], values[1], values[2], values[3]));
        }
        in.close();
        System.out.println("STG-Lib successfully loaded " + BOOTS.size() + " boots, " + GODS.size() + " gods, and " + ITEMS.size() + " items.");
    }

    // Implementing Fisher–Yates shuffle
    private static void shufflePositions(Positions[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Positions a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public Team generateTeam(int size, boolean forceOffensive, boolean forceDefensive, boolean forceBalanced, boolean forceBoots) {
        isForcingOffensive = forceOffensive;
        isForcingDefensive = forceDefensive;
        isForcingBalanced = forceBalanced;
        isForcingBoots = forceBoots;
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
                Player loadout = makeLoadout(positions[(int) (Math.random() * (positions.length))]);
                team.add(loadout);
            }
            boolean dupes = true;
            while (dupes) {
                dupes = false;
                for (int i = 0; i < size; i++) {
                    Player currentPlayer = team.getPlayer(i);
                    for (int j = 0; j < size; j++) {
                        if ((i != j) && currentPlayer.getGod().equals(team.getPlayer(j).getGod())) {
                            dupes = true;
                            team.set(j, makeLoadout(positions[(int) (Math.random() * (positions.length))]));
                            break;
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
        List<Item> playerBuild = null;
        List<Item> build;

        switch (position) {

            case MAGE:
                player = getGod("Mage");
                build = generateBuild(player);
                playerBuild = processBuild(player, build);
                break;

            case GUARDIAN:
                player = getGod("Guardian");
                build = generateBuild(player);
                playerBuild = processBuild(player, build);
                break;

            case WARRIOR:
                player = getGod("Warrior");
                build = generateBuild(player);
                playerBuild = processBuild(player, build);
                break;

            case ASSASSIN:
                player = getGod("Assassin");
                build = generateBuild(player);
                playerBuild = processBuild(player, build);
                break;

            case HUNTER:
                player = getGod("Hunter");
                build = generateBuild(player);
                playerBuild = processBuild(player, build);
                break;

            default:
                break;
        }

        return new Player(player, playerBuild);
    }

    private List<Item> processBuild(God god, List<Item> oBuild) {
        List<Item> build = oBuild;
        List<String> buildItems = new ArrayList<>();
        List<Item> playerBuild;
        while (true) {
            buildItems.clear();
            for (Item i : build) {
                buildItems.add(i.toString());
            }
            if (god.getPosition().equals("Hunter") && (buildItems.contains("Hastened Katana") || buildItems.contains("Stone Cutting Sword") || buildItems.contains("Masamune") || buildItems.contains("Golden Blade") || buildItems.contains("Rangda's Mask"))) {
                build = generateBuild(god);
            } else if ((god.getPosition().equals("Assassin") || god.getPosition().equals("Hunter") || god.getPosition().equals("Mage")) && (buildItems.contains("Rangda's Mask") || (buildItems.contains("Lono's Mask") && buildItems.contains("Bumba's Mask")))) {
                build = generateBuild(god);
            } else if ((god.getPosition().equals("Warrior") || god.getPosition().equals("Guardian")) && (buildItems.contains("Lono's Mask") || (buildItems.contains("Rangda's Mask") && buildItems.contains("Bumba's Mask")))) {
                build = generateBuild(god);
            } else if ((god.getPosition().equals("Assassin") || god.getPosition().equals("Hunter") || god.getPosition().equals("Mage")) && isForcingOffensive) {
                int offensiveCount = 0;
                for (Item i : build) {
                    if (i.isOffensive()) {
                        offensiveCount++;
                    }
                }
                if (offensiveCount < 6) {
                    build = generateBuild(god);
                } else {
                    break;
                }
            } else if ((god.getPosition().equals("Warrior") || god.getPosition().equals("Guardian")) && isForcingDefensive) {
                int defensiveCount = 0;
                for (Item i : build) {
                    if (i.isDefensive()) {
                        defensiveCount++;
                    }
                }
                if ((god.getPosition().equals("Guardian") && defensiveCount < 6) || (god.getPosition().equals("Warrior") && defensiveCount < 5)) {
                    build = generateBuild(god);
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        playerBuild = build;
        buildItems.clear();
        return playerBuild;
    }

    private List<Item> generateBuild(God god) {
        List<Item> build = new ArrayList<>();
        LinkedHashSet<Item> generation = new LinkedHashSet<>();
        String type = god.getPosition();
        Item newItem;
        if (type.equals("Assassin") || type.equals("Hunter") || type.equals("Warrior")) {
            switch (type.toLowerCase()) {
                case "assassin":
                case "hunter":
                    if (god.getName().equals("Ratatoskr")) {
                        generation.add(new Item("Acorn of Yggdrasil", "true", "false", "OFFENSE"));
                    } else {
                        if ((int)(Math.random() * 100) > 35  && !isForcingBoots) {
                            generation.add(getItem("physical"));
                        } else {
                            generation.add(getPhysicalBoot(isForcingOffensive));
                        }
                    }
                    break;
                case "warrior":
                    if ((int)(Math.random() * 100) > 35 && !isForcingBoots) {
                        generation.add(getItem("physical"));
                    } else {
                        generation.add(getPhysicalBoot(false));
                    }
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
            switch (god.getPosition().toLowerCase()) {
                case "mage":
                    if ((int)(Math.random() * 100) > 35 && !isForcingBoots) {
                        generation.add(getItem("magical"));
                    } else {
                        generation.add(getMagicalBoot(isForcingOffensive, false));
                    }
                    break;
                case "guardian":
                    if ((int)(Math.random() * 100) > 35 && !isForcingBoots) {
                        generation.add(getItem("magical"));
                    } else {
                        generation.add(getMagicalBoot(false, isForcingDefensive));
                    }
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
        boolean physical = type.equalsIgnoreCase("physical");
        item = ITEMS.get((int) (Math.random() * (ITEMS.size())));
        if (physical) {
            while (item.isMagical() && !item.isPhysical()) {
                item = ITEMS.get((int) (Math.random() * (ITEMS.size())));
            }
        } else {
            while (item.isPhysical() && !item.isMagical()) {
                item = ITEMS.get((int) (Math.random() * (ITEMS.size())));
            }
        }
        return item;
    }

    private God getGod(String position) {
        God god = GODS.get((int) (Math.random() * (GODS.size())));
        while (!god.getPosition().equalsIgnoreCase(position)) {
            god = GODS.get((int) (Math.random() * (GODS.size())));
        }
        return god;
    }

    public List<Item> getBOOTS() {
        return BOOTS;
    }

    public List<Item> getITEMS() {
        return ITEMS;
    }

    public List<God> getGODS() {
        return GODS;
    }

    public List<String> getGodsAsStrings() {
        List<String> returnList = new ArrayList<>();
        for (God i : GODS) {
            returnList.add(i.getName());
        }
        return returnList;
    }

    public List<String> getItemsAsStrings() {
        List<String> returnList = new ArrayList<>();
        for (Item i : ITEMS) {
            returnList.add(i.toString());
        }
        return returnList;
    }
}