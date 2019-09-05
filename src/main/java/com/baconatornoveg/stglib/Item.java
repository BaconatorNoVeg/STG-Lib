package com.baconatornoveg.stglib;

public class Item {

    private String name;
    private String itemType;
    private String[] availability;
    private boolean isPhysical;
    private boolean isMagical;

    protected Item(String name, String isPhysical, String isMagical, String itemType, String[] availability) {
        this.name = name;
        this.isPhysical = Boolean.parseBoolean(isPhysical);
        this.isMagical = Boolean.parseBoolean(isMagical);
        this.itemType = itemType;
        this.availability = availability;
    }

    public boolean isPhysical() {
        return isPhysical;
    }

    public boolean isMagical() {
        return isMagical;
    }

    public boolean isMask() {
        return "Lono's Mask".equalsIgnoreCase(name) || "Rangda's Mask".equalsIgnoreCase(name) || "Bumba's Mask".equalsIgnoreCase(name);
    }

    public boolean available(God god) {
        switch (god.getPosition().toLowerCase()) {
            case "assassin":
                return availability[0].equalsIgnoreCase("TRUE");

            case "hunter":
                return availability[1].equalsIgnoreCase("TRUE");

            case "mage":
                return availability[2].equalsIgnoreCase("TRUE");

            case "warrior":
                return availability[3].equalsIgnoreCase("TRUE");

            case "guardian":
                return availability[4].equalsIgnoreCase("TRUE");

            default:
                return false;
        }
    }

    public boolean isOffensive() {
        return itemType != null && (itemType.equals("OFFENSE") || itemType.equals("BOTH"));
    }

    public boolean isDefensive() {
        return itemType != null && (itemType.equals("DEFENSE") || itemType.equals("BOTH"));
    }

    public String toString() {
        return this.name;
    }
}
