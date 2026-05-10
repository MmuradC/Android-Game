package com.mobil.bmt342.bmtcardgame.model;

public class Enemy {
    public static final String INTENT_ATTACK = "attack";
    public static final String INTENT_BLOCK = "block";

    private final int id;
    private final String name;
    private final int maxHp;
    private int currentHp;
    private int block;
    private String intentType;
    private int intentValue;
    private String intentName;
    private int turnIndex;

    public Enemy(int id, String name, int maxHp, String intentType, int intentValue) {
        this.id = id;
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.intentType = intentType;
        this.intentValue = intentValue;
        this.intentName = INTENT_BLOCK.equals(intentType) ? "Guard" : "Strike";
        planIntent();
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

    public String getIntentName() {
        return intentName;
    }

    public void advanceIntent() {
        turnIndex++;
        planIntent();
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
            return "Intent: " + intentName + " - gain " + intentValue + " block";
        }
        return "Intent: " + intentName + " - attack for " + intentValue;
    }

    private void planIntent() {
        switch (id) {
            case 2:
                planTowerIntent();
                break;
            case 3:
                planDeathIntent();
                break;
            case 4:
                planDevilIntent();
                break;
            case 5:
                planHermitIntent();
                break;
            case 6:
                planWorldIntent();
                break;
            case 1:
            default:
                planMagicianIntent();
                break;
        }
    }

    private void setIntent(String name, String type, int value) {
        intentName = name;
        intentType = type;
        intentValue = value;
    }

    private void planMagicianIntent() {
        int step = turnIndex % 3;
        if (step == 0) {
            setIntent("Spark", INTENT_ATTACK, 6);
        } else if (step == 1) {
            setIntent("Sleight", INTENT_BLOCK, 6);
        } else {
            setIntent("Flare", INTENT_ATTACK, 9);
        }
    }

    private void planTowerIntent() {
        int step = turnIndex % 3;
        if (step == 0) {
            setIntent("Crack", INTENT_ATTACK, 7);
        } else if (step == 1) {
            setIntent("Brace", INTENT_BLOCK, 8);
        } else {
            setIntent("Collapse", INTENT_ATTACK, 14);
        }
    }

    private void planDeathIntent() {
        int step = turnIndex % 3;
        if (step == 0) {
            setIntent("Scythe", INTENT_ATTACK, 8);
        } else if (step == 1) {
            setIntent("Stillness", INTENT_BLOCK, 7);
        } else {
            setIntent("Reap", INTENT_ATTACK, 12);
        }
    }

    private void planDevilIntent() {
        int step = turnIndex % 2;
        if (step == 0) {
            setIntent("Tempt", INTENT_ATTACK, 9);
        } else {
            setIntent("Chains", INTENT_BLOCK, 10);
        }
    }

    private void planHermitIntent() {
        int step = turnIndex % 3;
        if (step == 0) {
            setIntent("Lantern", INTENT_BLOCK, 12);
        } else if (step == 1) {
            setIntent("Staff", INTENT_ATTACK, 7);
        } else {
            setIntent("Hidden Path", INTENT_BLOCK, 8);
        }
    }

    private void planWorldIntent() {
        int step = turnIndex % 4;
        if (step == 0) {
            setIntent("Orbit", INTENT_BLOCK, 10);
        } else if (step == 1) {
            setIntent("Turning", INTENT_ATTACK, 10);
        } else if (step == 2) {
            setIntent("Completion", INTENT_ATTACK, 15);
        } else {
            setIntent("Balance", INTENT_BLOCK, 12);
        }
    }
}
