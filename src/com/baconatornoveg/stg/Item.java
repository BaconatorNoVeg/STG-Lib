package com.baconatornoveg.stg;

public class Item {

    private String name;
    private String itemType;
    private boolean isPhysical;
    private boolean isMagical;

    protected Item(String name, String isPhysical, String isMagical, String itemType) {
        this.name = name;
        this.isPhysical = Boolean.parseBoolean(isPhysical);
        this.isMagical = Boolean.parseBoolean(isMagical);
        this.itemType = itemType;
    }

    public boolean isPhysical() {
        return isPhysical;
    }

    public boolean isMagical() {
        return isMagical;
    }

    public boolean isOffensive() { return itemType != null && (itemType.equals("OFFENSE") || itemType.equals("BOTH")); }

    public boolean isDefensive() { return itemType != null && (itemType.equals("DEFENSE") || itemType.equals("BOTH")); }

    public String toString() {
        return this.name;
    }
}
