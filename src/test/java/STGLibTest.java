import com.baconatornoveg.stglib.Item;
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
    private final int maxTests = 100000;
    private Player player;

    public STGLibTest() {
        System.out.println("Running tests");
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
            in.close();
            godsInitialSize = gods.size();
            in = new Scanner(new File("Lists/items.csv"));
            in.nextLine();

            while (in.hasNextLine()) {
                String currentLine = in.nextLine();
                String[] values = currentLine.split(",");
                items.add(values[0]);
            }
            in.close();
            itemsInitialSize = items.size();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        stg = new SmiteTeamGenerator();
        stg.getLists(true);
        stg.isForcingOffensive = false;
        stg.isForcingDefensive = false;
        stg.isForcingBalanced = false;

    }

    @Test
    public void testForAllGods() {

        int totalAttempts = 0;

        for (int i = 0; i < maxTests; i++) {
            totalAttempts++;
            player = stg.makeLoadout(SmiteTeamGenerator.Positions.values()[(int)(Math.random() * 5)]);
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
            System.err.println("Failed to confirm all " + godsInitialSize + " gods being generated in " + maxTests + " total generations.");
            System.err.println("Leftover gods: " + gods.toString());
            fail();
        } else {
            System.out.println("Successfully confirmed all " + godsInitialSize + " gods being generated in " + totalAttempts + " generations.");
            System.out.println("Leftover gods: " + gods.toString());
        }
    }

    @Test
    public void testForAllItems() {
        int totalAttempts = 0;
        ArrayList<String> testedItems = new ArrayList<>();
        ArrayList<String> missingItems = items;
        for (int i = 0; i < maxTests; i++) {
            totalAttempts++;
            player = stg.makeLoadout(SmiteTeamGenerator.Positions.values()[(int)(Math.random() * 5)]);
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
            System.err.println("Failed to confirm all " + itemsInitialSize + " items being generated in " + maxTests + " total generations.");
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
    public void testForKatanasOnHuntersForceOffensive() {
        int totalAttempts = 0;
        stg.isForcingOffensive = true;
        for (int i = 0; i < maxTests; i++) {
            totalAttempts++;
            player = stg.makeLoadout(SmiteTeamGenerator.Positions.HUNTER);
            ArrayList<String> build = player.getBuild();
            if (build.contains("Hastened Katana") || build.contains("Masamune") || build.contains("Stone Cutting Sword") || build.contains("Golden Blade")) {
                System.err.println("A hunter build contains a forbidden hunter item.");
                System.err.println("Build: " + player.toString());
                fail("A hunter build contains a forbidden hunter item.");
            }
        }
    }

    @Test
    public void testForKatanasOnHunters() {
        int totalAttempts = 0;
        stg.isForcingOffensive = false;
        for (int i = 0; i < maxTests; i++) {
            totalAttempts++;
            player = stg.makeLoadout(SmiteTeamGenerator.Positions.HUNTER);
            ArrayList<String> build = player.getBuild();
            if (build.contains("Hastened Katana") || build.contains("Masamune") || build.contains("Stone Cutting Sword") || build.contains("Golden Blade")) {
                System.err.println("A hunter build contains a forbidden hunter item.");
                System.err.println("Build: " + player.toString());
                fail("A hunter build contains a forbidden hunter item.");
            }
        }
    }

    @Test
    public void testForMasksOnWrongTypesForceOffensive() {
        int totalAttempts = 0;
        stg.isForcingOffensive = true;
        for (int i = 0; i < maxTests; i++) {
            totalAttempts++;
            player = stg.makeLoadout(SmiteTeamGenerator.Positions.values()[(int)(Math.random() * 5)]);
            ArrayList<String> build = player.getBuild();
            // Rangda's mask on assassins, hunters, or mages
            if ((player.getGod().getPosition().equals("Assassin") || player.getGod().getPosition().equals("Hunter") || player.getGod().getPosition().equals("Mage")) && build.contains("Rangda's Mask")) {
                fail("Rangda's mask is on a " + player.getGod().getPosition() + ".");
            }
            // Lono's mask on warriors or guardians
            else if ((player.getGod().getPosition().equals("Warrior") || player.getGod().getPosition().equals("Guardian")) && build.contains("Lono's Mask")) {
                fail("Lono's mask is on a " + player.getGod().getPosition() + ".");
            }
        }
    }

    @Test
    public void testForMasksOnWrongTypesForceDefensive() {
        int totalAttempts = 0;
        stg.isForcingDefensive = true;
        for (int i = 0; i < maxTests; i++) {
            totalAttempts++;
            player = stg.makeLoadout(SmiteTeamGenerator.Positions.values()[(int)(Math.random() * 5)]);
            ArrayList<String> build = player.getBuild();
            // Rangda's mask on assassins, hunters, or mages
            if ((player.getGod().getPosition().equals("Assassin") || player.getGod().getPosition().equals("Hunter") || player.getGod().getPosition().equals("Mage")) && build.contains("Rangda's Mask")) {
                fail("Rangda's mask is on a " + player.getGod().getPosition() + ".");
            }
            // Lono's mask on warriors or guardians
            else if ((player.getGod().getPosition().equals("Warrior") || player.getGod().getPosition().equals("Guardian")) && build.contains("Lono's Mask")) {
                fail("Lono's mask is on a " + player.getGod().getPosition() + ".");
            }
        }
    }

    @Test
    public void testForMultipleMasksOnBuild() {
        int totalAttempts = 0;
        for (int i = 0; i < maxTests; i++) {
            totalAttempts++;
            player = stg.makeLoadout(SmiteTeamGenerator.Positions.values()[(int)(Math.random() * 5)]);
            ArrayList<String> build = player.getBuild();
            int maskCount = 0;
            for (String j : build) {
                if (j.equals("Bumba's Mask") || j.equals("Lono's Mask") || j.equals("Rangda's Mask")) {
                    maskCount++;
                }
            }
            if (maskCount > 1) {
                fail("There is more than one mask on a build.\nOffending build: " + player.toString());
            }
        }
    }

    @Test
    public void testForMasksOnWrongTypes() {
        int totalAttempts = 0;
        stg.isForcingOffensive = false;
        stg.isForcingDefensive = false;
        for (int i = 0; i < maxTests; i++) {
            totalAttempts++;
            player = stg.makeLoadout(SmiteTeamGenerator.Positions.values()[(int)(Math.random() * 5)]);
            ArrayList<String> build = player.getBuild();
            // Rangda's mask on assassins, hunters, or mages
            if ((player.getGod().getPosition().equals("Assassin") || player.getGod().getPosition().equals("Hunter") || player.getGod().getPosition().equals("Mage")) && build.contains("Rangda's Mask")) {
                fail("Rangda's mask is on a " + player.getGod().getPosition() + ".");
            }
            // Lono's mask on warriors or guardians
            else if ((player.getGod().getPosition().equals("Warrior") || player.getGod().getPosition().equals("Guardian")) && build.contains("Lono's Mask")) {
                fail("Lono's mask is on a " + player.getGod().getPosition() + ".");
            }
        }
    }

    @Test
    public void getFullStyleBuildChances() {
        System.out.println("This test is designed to check the percent chance of full builds of a particular play style (Offense, Defense). This test is NOT going to fail.");
        System.out.println("Depending on the 'testMultFactor' value, this test can take anywhere from a few seconds to a few minutes. Just remember, the higher the value for that variable, the more accurate the results are going to be.");
        int testMultFactor = 10;
        System.out.println("'testMultFactor' value is " + testMultFactor);
        int totalAttempts = 0;
        int offensiveTotalOnDefensives = 0;
        int offensiveTotalOnOffensives = 0;
        int defensiveTotalOnDefensives = 0;
        int defensiveTotalOnOffensives = 0;
        stg.isForcingOffensive = false;
        stg.isForcingDefensive = false;
        for (int i = 0; i < maxTests*testMultFactor; i++) {
            totalAttempts++;
            player = stg.makeLoadout(SmiteTeamGenerator.Positions.values()[(int)(Math.random() * 5)]);
            boolean defensiveGod = false;
            if (player.getGod().getPosition().equals("Warrior") || player.getGod().getPosition().equals("Guardian")) {
                defensiveGod = true;
            }
            int offensiveItems = 0;
            int defensiveItems = 0;
            for (Item j : player.getBuildAsItems()) {
                if (j.isOffensive()) {
                    offensiveItems++;
                } else if (j.isDefensive()) {
                    defensiveItems++;
                }
            }
            if (offensiveItems == 6) {
                if (defensiveGod) {
                    offensiveTotalOnDefensives++;
                } else {
                    offensiveTotalOnOffensives++;
                }
            } else if (defensiveItems == 6) {
                if (defensiveGod) {
                    defensiveTotalOnDefensives++;
                } else {
                    defensiveTotalOnOffensives++;
                }
            }
        }
        System.out.println("Full Offensive Builds on Offensive Gods Count: " + offensiveTotalOnOffensives + " | Percent Chance: " + (((float)offensiveTotalOnOffensives/(float)totalAttempts) * 100) + "%");
        System.out.println("Full Offensive Builds on Defensive Gods Count: " + offensiveTotalOnDefensives + " | Percent Chance: " + (((float)offensiveTotalOnDefensives/(float)totalAttempts) * 100) + "%");
        System.out.println("Full Defensive Builds on Offensive Gods Count: " + defensiveTotalOnOffensives + " | Percent Chance: " + (((float)defensiveTotalOnOffensives/(float)totalAttempts) * 100) + "%");
        System.out.println("Full Defensive Builds on Defensive Gods Count: " + defensiveTotalOnDefensives + " | Percent Chance: " + (((float)defensiveTotalOnDefensives/(float)totalAttempts) * 100) + "%");
        System.out.println();
        System.out.println("Statistics calculated out of the total " + totalAttempts + " build generations for this particular test.");
    }

}
