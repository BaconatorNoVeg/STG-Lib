import com.baconatornoveg.stglib.Player;
import com.baconatornoveg.stglib.SmiteTeamGenerator;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static junit.framework.TestCase.fail;

public class STGLibTest {

    private SmiteTeamGenerator stg;
    private ArrayList<String> gods;
    private ArrayList<String> items;
    private int godsInitialSize;
    private int itemsInitialSize;

    public STGLibTest() {
        Scanner in;
        gods = new ArrayList<>();
        items = new ArrayList<>();

        try {

            in = new Scanner(new File("Lists/gods.csv"));
            in.nextLine();

            while(in.hasNextLine()) {
                String currentLine = in.nextLine();
                String[] values = currentLine.split(",");
                gods.add(values[0]);
            }
            System.out.println("Gods: " + gods.toString());
            in.close();
            godsInitialSize = gods.size();
            in = new Scanner(new File("Lists/items.csv"));
            in.nextLine();

            while (in.hasNextLine()) {
                String currentLine = in.nextLine();
                String[] values = currentLine.split(",");
                items.add(values[0]);
            }
            System.out.println("Items: " + items.toString());
            in.close();
            itemsInitialSize = items.size();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        stg = new SmiteTeamGenerator();
        stg.getLists(true);
        stg.isForcingOffensive = true;
        stg.isForcingDefensive = true;
        stg.isForcingBalanced = true;
        System.out.println("Forcing Offensive: " + stg.isForcingOffensive);
        System.out.println("Forcing Defensive: " + stg.isForcingDefensive);
        System.out.println("Forcing Balanced " + stg.isForcingBalanced);

    }

    @Test
    public void testForAllGods() {

        int maxTries = 1000000;
        int totalAttempts = 0;

        for (int i = 0; i < maxTries; i++) {
            totalAttempts++;
            Player player = stg.makeLoadout(SmiteTeamGenerator.Positions.values()[(int)(Math.random() * 5)]);
            String god = player.getGod().getName();
            //System.out.println(god);
            for (String g : gods) {
                if (god.equals(g)) {
                    gods.remove(g);
                    break;
                }
            }
            if (gods.size() <= 0) {
                break;
            }
        }
        if (!(gods.size() <= 0)) {
            System.err.println("Failed to confirm all " + godsInitialSize + " gods being generated in " + maxTries + " total generations.");
            System.err.println("Leftover gods: " + gods.toString());
            fail();
        } else {
            System.out.println("Successfully confirmed all " + godsInitialSize + " gods being generated in " + totalAttempts + " generations.");
            System.out.println("Leftover gods: " + gods.toString());
        }
    }

    @Test
    public void testForAllItems() {
        int maxTries = 1000000;
        int totalAttempts = 0;
        ArrayList<String> testedItems = new ArrayList<>();
        ArrayList<String> missingItems = items;
        for (int i = 0; i < maxTries; i++) {
            totalAttempts++;
            Player player = stg.makeLoadout(SmiteTeamGenerator.Positions.values()[(int)(Math.random() * 5)]);
            ArrayList<String> build = player.getBuild();
            for (String item : items) {
                for (String itm : build) {
                    if (itm.equals(item) && !testedItems.contains(item)) {
                        testedItems.add(item);
                    }
                }
            }
            Collections.sort(items);
            Collections.sort(testedItems);
            if (items.size() == testedItems.size()) {
                if (items.equals(testedItems)) {
                    break;
                }
            }
        }
        missingItems.removeAll(testedItems);
        if (!(itemsInitialSize == testedItems.size()) && !(missingItems.size() == 0)) {
            System.err.println("Failed to confirm all " + itemsInitialSize + " items being generated in " + maxTries + " total generations.");
            System.err.println(testedItems.size() + " items succeeded out of " + itemsInitialSize);
            System.err.println("Tested items: \n" + testedItems.toString());
            System.err.println("Missing items: \n" + missingItems.toString());
            fail();
        } else {
            System.out.println("Successfully confirmed all " + itemsInitialSize + " items being generated in " + totalAttempts + " generations.");
            System.out.println("Tested items: \n" + testedItems.toString());
            System.out.println("Missing items: \n" + missingItems.toString());
        }
    }

    @Test
    public void testForKatanasOnHunters() {
        int maxTries = 1000000;
        int totalAttempts = 0;
        for (int i = 0; i < maxTries; i++) {
            totalAttempts++;
            Player player = stg.makeLoadout(SmiteTeamGenerator.Positions.HUNTER);
            ArrayList<String> build = player.getBuild();
            if (build.contains("Hastened Katana") || build.contains("Masamune") || build.contains("Stone Cutting Sword") || build.contains("Golden Blade")) {
                System.err.println("A hunter build contains a forbidden hunter item.");
                System.err.println("Build: " + player.toString());
                fail("A hunter build contains a forbidden hunter item.");
            }
        }
    }

}
