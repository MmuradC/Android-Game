package com.mobil.bmt342.bmtcardgame.model;

public class Card {
    public static final String DAMAGE = "damage";
    public static final String BLOCK = "block";
    public static final String HEAL = "heal";
    public static final String DRAW = "draw";
    public static final String LIFESTEAL = "lifesteal";
    public static final String ENERGY = "energy";

    private final int id;
    private final String name;
    private final String family;
    private final int cost;
    private final String effectType;
    private final int value;
    private final String description;

    public Card(int id, String name, String family, int cost, String effectType, int value, String description) {
        this.id = id;
        this.name = name;
        this.family = family;
        this.cost = cost;
        this.effectType = effectType;
        this.value = value;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFamily() {
        return family;
    }

    public int getCost() {
        return cost;
    }

    public String getEffectType() {
        return effectType;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
