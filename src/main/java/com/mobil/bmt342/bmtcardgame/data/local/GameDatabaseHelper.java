package com.mobil.bmt342.bmtcardgame.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mobil.bmt342.bmtcardgame.model.Card;
import com.mobil.bmt342.bmtcardgame.model.Enemy;

public class GameDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "tarot_cards.db";
    public static final int DATABASE_VERSION = 6;

    public static final String TABLE_CARDS = "cards";
    public static final String TABLE_ENEMIES = "enemies";
    public static final String TABLE_UNLOCKED_CARDS = "unlocked_cards";
    public static final String TABLE_CLAIMED_REWARDS = "claimed_rewards";
    public static final String TABLE_ENCOUNTER_REWARDS = "encounter_rewards";
    public static final String TABLE_REMOVED_CARDS = "removed_cards";
    public static final String TABLE_GAME_PROGRESS = "game_progress";

    public GameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CARDS + " (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "family TEXT NOT NULL, " +
                "cost INTEGER NOT NULL, " +
                "effect_type TEXT NOT NULL, " +
                "effect_value INTEGER NOT NULL, " +
                "description TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_ENEMIES + " (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "max_hp INTEGER NOT NULL, " +
                "intent_type TEXT NOT NULL, " +
                "intent_value INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_UNLOCKED_CARDS + " (" +
                "card_id INTEGER PRIMARY KEY)");

        db.execSQL("CREATE TABLE " + TABLE_CLAIMED_REWARDS + " (" +
                "encounter_id INTEGER NOT NULL, " +
                "card_id INTEGER NOT NULL, " +
                "PRIMARY KEY(encounter_id, card_id))");

        db.execSQL("CREATE TABLE " + TABLE_ENCOUNTER_REWARDS + " (" +
                "encounter_id INTEGER NOT NULL, " +
                "card_id INTEGER NOT NULL, " +
                "PRIMARY KEY(encounter_id, card_id))");

        db.execSQL("CREATE TABLE " + TABLE_REMOVED_CARDS + " (" +
                "card_id INTEGER PRIMARY KEY)");

        db.execSQL("CREATE TABLE " + TABLE_GAME_PROGRESS + " (" +
                "id INTEGER PRIMARY KEY, " +
                "run_counter INTEGER NOT NULL, " +
                "furthest_unlocked_encounter INTEGER NOT NULL, " +
                "discard_tokens INTEGER NOT NULL)");

        seedCards(db);
        seedEnemies(db);
        seedEncounterRewards(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENEMIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNLOCKED_CARDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLAIMED_REWARDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENCOUNTER_REWARDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMOVED_CARDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME_PROGRESS);
        onCreate(db);
    }

    private void seedCards(SQLiteDatabase db) {
        insertCard(db, 1, "Ace of Wands", "Wands", 1, Card.DAMAGE, 7, "Quick strike. Deal 7 damage.");
        insertCard(db, 2, "Five of Wands", "Wands", 2, Card.DAMAGE, 15, "Heavy strike. Deal 15 damage.");
        insertCard(db, 3, "Ace of Cups", "Cups", 1, Card.HEAL, 5, "Restore 5 HP.");
        insertCard(db, 4, "Two of Cups", "Cups", 1, Card.LIFESTEAL, 5, "Deal 5 damage. Restore 5 HP.");
        insertCard(db, 5, "Ace of Swords", "Swords", 0, Card.DRAW, 1, "Free. Draw 1 card.");
        insertCard(db, 6, "Three of Swords", "Swords", 1, Card.DRAW, 2, "Draw 2 cards.");
        insertCard(db, 7, "Ace of Pentacles", "Pentacles", 1, Card.BLOCK, 7, "Gain 7 block.");
        insertCard(db, 8, "Five of Pentacles", "Pentacles", 2, Card.BLOCK, 16, "Brace hard. Gain 16 block.");
        insertCard(db, 9, "Two of Swords", "Swords", 0, Card.ENERGY, 1, "Free. Restore 1 energy.");
        insertCard(db, 10, "Seven of Wands", "Wands", 2, Card.DAMAGE, 20, "Focused fire. Deal 20 damage.");
        insertCard(db, 11, "Four of Pentacles", "Pentacles", 1, Card.BLOCK, 12, "Gain 12 block.");
        insertCard(db, 12, "Six of Cups", "Cups", 2, Card.HEAL, 12, "Restore 12 HP.");
        insertCard(db, 13, "Eight of Wands", "Wands", 1, Card.DAMAGE, 12, "Fast strike. Deal 12 damage.");
        insertCard(db, 14, "Four of Swords", "Swords", 1, Card.DRAW, 3, "Draw 3 cards.");
        insertCard(db, 15, "Six of Pentacles", "Pentacles", 1, Card.BLOCK, 10, "Gain 10 block.");
        insertCard(db, 16, "Three of Cups", "Cups", 1, Card.LIFESTEAL, 8, "Deal 8 damage. Restore 8 HP.");
        insertCard(db, 17, "Nine of Wands", "Wands", 3, Card.DAMAGE, 28, "All-in strike. Deal 28 damage.");
        insertCard(db, 18, "Seven of Swords", "Swords", 0, Card.ENERGY, 2, "Free. Restore 2 energy.");
        insertCard(db, 19, "Nine of Pentacles", "Pentacles", 3, Card.BLOCK, 26, "Gain 26 block.");
        insertCard(db, 20, "Queen of Cups", "Cups", 2, Card.HEAL, 18, "Restore 18 HP.");
        insertCard(db, 21, "Page of Swords", "Swords", 0, Card.DRAW, 2, "Free. Draw 2 cards.");
        insertCard(db, 22, "King of Wands", "Wands", 3, Card.DAMAGE, 34, "Royal flame. Deal 34 damage.");
        insertCard(db, 23, "Queen of Pentacles", "Pentacles", 2, Card.BLOCK, 22, "Gain 22 block.");
        insertCard(db, 24, "Ten of Cups", "Cups", 2, Card.LIFESTEAL, 12, "Deal 12 damage. Restore 12 HP.");
    }

    private void seedEnemies(SQLiteDatabase db) {
        insertEnemy(db, 1, "I - The Magician", 32, Enemy.INTENT_ATTACK, 6);
        insertEnemy(db, 2, "XVI - The Tower", 40, Enemy.INTENT_ATTACK, 7);
        insertEnemy(db, 3, "XIII - Death", 44, Enemy.INTENT_ATTACK, 8);
        insertEnemy(db, 4, "XV - The Devil", 48, Enemy.INTENT_ATTACK, 9);
        insertEnemy(db, 5, "IX - The Hermit", 42, Enemy.INTENT_BLOCK, 12);
        insertEnemy(db, 6, "XXI - The World", 72, Enemy.INTENT_BLOCK, 10);
    }

    private void insertCard(SQLiteDatabase db, int id, String name, String family, int cost,
                            String effectType, int effectValue, String description) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("family", family);
        values.put("cost", cost);
        values.put("effect_type", effectType);
        values.put("effect_value", effectValue);
        values.put("description", description);
        db.insert(TABLE_CARDS, null, values);
    }

    private void insertEnemy(SQLiteDatabase db, int id, String name, int maxHp,
                             String intentType, int intentValue) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("max_hp", maxHp);
        values.put("intent_type", intentType);
        values.put("intent_value", intentValue);
        db.insert(TABLE_ENEMIES, null, values);
    }

    private void seedEncounterRewards(SQLiteDatabase db) {
        insertEncounterReward(db, 1, 4);
        insertEncounterReward(db, 1, 6);
        insertEncounterReward(db, 1, 9);
        insertEncounterReward(db, 2, 10);
        insertEncounterReward(db, 2, 11);
        insertEncounterReward(db, 2, 12);
        insertEncounterReward(db, 3, 13);
        insertEncounterReward(db, 3, 14);
        insertEncounterReward(db, 3, 15);
        insertEncounterReward(db, 4, 16);
        insertEncounterReward(db, 4, 17);
        insertEncounterReward(db, 4, 18);
        insertEncounterReward(db, 5, 19);
        insertEncounterReward(db, 5, 20);
        insertEncounterReward(db, 5, 21);
        insertEncounterReward(db, 6, 22);
        insertEncounterReward(db, 6, 23);
        insertEncounterReward(db, 6, 24);
    }

    private void insertEncounterReward(SQLiteDatabase db, int encounterId, int cardId) {
        ContentValues values = new ContentValues();
        values.put("encounter_id", encounterId);
        values.put("card_id", cardId);
        db.insert(TABLE_ENCOUNTER_REWARDS, null, values);
    }
}
