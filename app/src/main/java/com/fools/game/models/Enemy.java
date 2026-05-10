package com.fools.game.models;

public abstract class Enemy extends Entity {
    private String nextIntent;

    public Enemy(String name, int maxHealth) {
        super(name, maxHealth);
        this.nextIntent = "Waiting...";
    }

    public String getNextIntent() { return nextIntent; }
    public void setNextIntent(String intent) { this.nextIntent = intent; }

    // To be implemented by specific Bosses (e.g., TheTower, TheMagician)
    public abstract void performAction(Player player);
    public abstract void planNextAction();
}
