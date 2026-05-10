package com.fools.game.models;

public class Player extends Entity {
    private int maxEnergy;
    private int currentEnergy;
    private Deck deck;

    public Player(String name, int maxHealth, int maxEnergy) {
        super(name, maxHealth);
        this.maxEnergy = maxEnergy;
        this.currentEnergy = maxEnergy;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Deck getDeck() {
        return deck;
    }

    public void drawCards(int amount) {
        if (deck != null) deck.draw(amount);
    }

    public boolean spendEnergy(int amount) {
        if (this.currentEnergy >= amount) {
            this.currentEnergy -= amount;
            return true;
        }
        return false;
    }

    public void restoreEnergy() {
        this.currentEnergy = this.maxEnergy;
    }

    public int getCurrentEnergy() { return currentEnergy; }
    public int getMaxEnergy() { return maxEnergy; }
}
