package com.fools.game.models;

public abstract class Entity {
    private String name;
    private int maxHealth;
    private int currentHealth;
    private int block;

    public Entity(String name, int maxHealth) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.block = 0;
    }

    public void takeDamage(int damage) {
        if (this.block >= damage) {
            this.block -= damage;
        } else {
            int remainingDamage = damage - this.block;
            this.block = 0;
            this.currentHealth -= remainingDamage;
            if (this.currentHealth < 0) {
                this.currentHealth = 0;
            }
        }
    }

    public void addBlock(int amount) {
        this.block += amount;
    }

    public void heal(int amount) {
        this.currentHealth += amount;
        if (this.currentHealth > this.maxHealth) {
            this.currentHealth = this.maxHealth;
        }
    }

    public void resetBlock() {
        this.block = 0;
    }

    // Getters
    public String getName() { return name; }
    public int getMaxHealth() { return maxHealth; }
    public int getCurrentHealth() { return currentHealth; }
    public int getBlock() { return block; }
    public boolean isDead() { return currentHealth <= 0; }
}
