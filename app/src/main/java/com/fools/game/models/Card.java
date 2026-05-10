package com.fools.game.models;

public class Card {
    public enum Suit {
        WANDS,      // Damage
        CUPS,       // Heal/Sustain
        SWORDS,     // Draw/Manipulate
        PENTACLES   // Block
    }

    private String name;
    private Suit suit;
    private int cost;
    private Effect effect;
    private String description;

    public Card(String name, Suit suit, int cost, String description, Effect effect) {
        this.name = name;
        this.suit = suit;
        this.cost = cost;
        this.description = description;
        this.effect = effect;
    }

    public void play(Entity source, Entity target) {
        if (effect != null) {
            effect.execute(source, target);
        }
    }

    // Getters
    public String getName() { return name; }
    public Suit getSuit() { return suit; }
    public int getCost() { return cost; }
    public String getDescription() { return description; }
}
