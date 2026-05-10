package com.mobil.bmt342.bmtcardgame.model;

public class PlayerState {
    private static final int MAX_ENERGY = 3;

    private final int maxHp;
    private int currentHp;
    private int block;
    private int energy;

    public PlayerState(int maxHp) {
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        resetTurn();
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getBlock() {
        return block;
    }

    public int getEnergy() {
        return energy;
    }

    public void resetTurn() {
        block = 0;
        energy = MAX_ENERGY;
    }

    public void resetForNewRun() {
        currentHp = maxHp;
        resetTurn();
    }

    public void spendEnergy(int amount) {
        energy = Math.max(0, energy - amount);
    }

    public void gainEnergy(int amount) {
        energy = Math.min(MAX_ENERGY, energy + Math.max(0, amount));
    }

    public void addBlock(int amount) {
        block += Math.max(0, amount);
    }

    public void heal(int amount) {
        currentHp = Math.min(maxHp, currentHp + Math.max(0, amount));
    }

    public void takeDamage(int amount) {
        int remaining = Math.max(0, amount);
        int blocked = Math.min(block, remaining);
        block -= blocked;
        remaining -= blocked;
        currentHp = Math.max(0, currentHp - remaining);
    }
}
