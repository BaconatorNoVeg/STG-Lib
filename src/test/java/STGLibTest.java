import com.baconatornoveg.stglib.God;
import com.baconatornoveg.stglib.Player;
import com.baconatornoveg.stglib.SmiteTeamGenerator;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

import static junit.framework.TestCase.fail;

public class STGLibTest {

    SmiteTeamGenerator stg;
    ArrayList<String> gods;
    ArrayList<String> items;
    int godsInitialSize;
    int itemsInitialSize;

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
            System.err.println("Failed to confirm all " + itemsInitialSize + " items being generated in " + maxTries + " total generations.");
            System.err.println("Leftover items: " + items.toString());
            fail();
        } else {
            System.out.println("Successfully confirmed all " + itemsInitialSize + " items being generated in " + totalAttempts + " generations.");
            System.out.println("Leftover items: " + items.toString());
        }
    }

    @Test
    public void testForAllItems() {
        int maxTries = 1000000;
        int totalAttempts = 0;

        for (int i = 0; i < maxTries; i++) {
            totalAttempts++;
            Player player = stg.makeLoadout(SmiteTeamGenerator.Positions.values()[(int)(Math.random() * 5)]);
            ArrayList<String> build = player.getBuild();
            for (String item : items) {
                for (String itm : build) {
                    if (itm.equals(item)) {
                        items.remove(item);
                    }
                }
            }
            if (items.size() <= 0) {
                break;
            }
        }
        if (!(items.size() <= 0)) {
            System.err.println("Failed to confirm all " + godsInitialSize + " gods being generated in " + maxTries + " total generations.");
            System.err.println("Leftover gods: " + gods.toString());
            fail();
        } else {
            System.out.println("Successfully confirmed all " + godsInitialSize + " gods being generated in " + totalAttempts + " generations.");
            System.out.println("Leftover gods: " + gods.toString());
        }
    }

}
