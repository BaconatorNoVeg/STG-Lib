package com.baconatornoveg.stg;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SmiteTeamGenerator {
    private final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private String bootsRemoteUrl = "https://raw.githubusercontent.com/BaconatorNoVeg/SmiteTeamGenerator/master/Lists/boots.csv";
    private String godsRemoteUrl = "https://raw.githubusercontent.com/BaconatorNoVeg/SmiteTeamGenerator/master/Lists/gods.csv";
    private String itemsRemoteUrl = "https://raw.githubusercontent.com/BaconatorNoVeg/SmiteTeamGenerator/master/Lists/items.csv";

    private final ArrayList<Item> BOOTS = new ArrayList<>();
    private final ArrayList<God> GODS = new ArrayList<>();
    private final ArrayList<Item> ITEMS = new ArrayList<>();

    public SmiteTeamGenerator() {

    }

    public void getLists(boolean local) {
        Scanner in;
        InputStream bootsFile = null;
        InputStream godsFile = null;
        InputStream itemsFile = null;
        if (!local) {
            LOGGER.info("Fetching the lists from Github...");
            try {
                URL bootsUrl = new URL(bootsRemoteUrl);
                URL godsUrl = new URL(godsRemoteUrl);
                URL itemsUrl = new URL(itemsRemoteUrl);
                bootsFile = bootsUrl.openStream();
                godsFile = godsUrl.openStream();
                itemsFile = itemsUrl.openStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.info("Using the local lists...");
            try {
                bootsFile = new FileInputStream("Lists/boots.csv");
                godsFile = new FileInputStream("Lists/gods.csv");
                itemsFile = new FileInputStream("Lists/items.csv");
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
        LOGGER.info("Smite Team Generator successfully loaded " + BOOTS.size() + " boots, " + GODS.size() + " gods, and " + ITEMS.size() + " items.");
    }
}