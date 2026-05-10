package com.mobil.bmt342.bmtcardgame.model;

public class Enemy {
    public static final String INTENT_ATTACK = "attack";
    public static final String INTENT_BLOCK = "block";

    private final int id;
    private final String name;
    private final int maxHp;
    private int currentHp;
    private int block;
    private final String intentType;
    private final int intentValue;

    public Enemy(int id, String name, int maxHp, String intentType, int intentValue) {
        this.id = id;
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.intentType = intentType;
        this.intentValue = intentValue;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMajorNumber() {
        int separator = name.indexOf(" - ");
        return separator > 0 ? name.substring(0, separator) : "";
    }

    public String getDisplayName() {
        int separator = name.indexOf(" - ");
        return separator > 0 ? name.substring(separator + 3) : name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = Math.max(0, Math.min(maxHp, currentHp));
    }

    public int getBlock() {
        return block;
    }

    public void addBlock(int amount) {
        block += Math.max(0, amount);
    }

    public void clearBlock() {
        block = 0;
    }

    public String getIntentType() {
        return intentType;
    }

    public int getIntentValue() {
        return intentValue;
    }

    public void takeDamage(int amount) {
        int remaining = Math.max(0, amount);
        int blocked = Math.min(block, remaining);
        block -= blocked;
        remaining -= blocked;
        setCurrentHp(currentHp - remaining);
    }

    public String getIntentText() {
        if (INTENT_BLOCK.equals(intentType)) {
            return "Intent: Guard, gain " + intentValue + " block";
        }
        return "Intent: Strike, attack for " + intentValue;
    }
}
