# STG-Lib
[![](https://jitpack.io/v/BaconatorNoVeg/STG-Lib.svg)](https://jitpack.io/#BaconatorNoVeg/STG-Lib)

A Java library that can be used to generate a random Smite team with random builds for the game Smite by Hi-Rez Studios.

# Usage Examples
Create the Generator Object:
```java
import com.baconatornoveg.stglib.*;

public static void main(String[] args) {
    SmiteTeamGenerator stg = new SmiteTeamGenerator();
    stg.getLists(false); // Fetches the latest lists. Must be called before any other stg methods.
}
```
Generate a team and output to console:
```java
int teamSize = 4;
boolean forceOffensive = true;
boolean forceDefensive = true;
boolean forceBalanced = false;
Team generatedTeam = stg.generateTeam(4, forceOffensive, forceDefensive, forceBalanced);
System.out.println(generatedTeam.toString());
```
The forceOffensive, forceDefensive, and forceBalanced flags can control how the generator builds the team.