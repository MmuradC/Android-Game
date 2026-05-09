package com.mobil.bmt342.bmtcardgame.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mobil.bmt342.bmtcardgame.model.Card;
import com.mobil.bmt342.bmtcardgame.model.Enemy;

public class GameDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "tarot_cards.db";
    public static final int DATABASE_VERSION = 2;

    public static final String TABLE_CARDS = "cards";
    public static final String TABLE_ENEMIES = "enemies";

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

        seedCards(db);
        seedEnemies(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENEMIES);
        onCreate(db);
    }

    private void seedCards(SQLiteDatabase db) {
        insertCard(db, 1, "Ace of Wands", "Wands", 1, Card.DAMAGE, 6, "Deal 6 damage.");
        insertCard(db, 2, "Three of Wands", "Wands", 1, Card.DAMAGE, 8, "Deal 8 damage.");
        insertCard(db, 3, "Five of Wands", "Wands", 2, Card.DAMAGE, 13, "Deal 13 damage.");
        insertCard(db, 4, "Ace of Cups", "Cups", 1, Card.HEAL, 4, "Heal 4 HP.");
        insertCard(db, 5, "Two of Cups", "Cups", 1, Card.LIFESTEAL, 5, "Deal 5 damage and heal 5 HP.");
        insertCard(db, 6, "Four of Cups", "Cups", 2, Card.HEAL, 9, "Heal 9 HP.");
        insertCard(db, 7, "Ace of Swords", "Swords", 0, Card.DRAW, 1, "Draw 1 card.");
        insertCard(db, 8, "Three of Swords", "Swords", 1, Card.DRAW, 2, "Draw 2 cards.");
        insertCard(db, 9, "Five of Swords", "Swords", 2, Card.DAMAGE, 10, "Deal 10 precise damage.");
        insertCard(db, 10, "Ace of Pentacles", "Pentacles", 1, Card.BLOCK, 5, "Gain 5 block.");
        insertCard(db, 11, "Three of Pentacles", "Pentacles", 1, Card.BLOCK, 8, "Gain 8 block.");
        insertCard(db, 12, "Five of Pentacles", "Pentacles", 2, Card.BLOCK, 14, "Gain 14 block.");
    }

    private void seedEnemies(SQLiteDatabase db) {
        insertEnemy(db, 1, "I - The Magician", 32, Enemy.INTENT_ATTACK, 7);
        insertEnemy(db, 2, "XVI - The Tower", 38, Enemy.INTENT_ATTACK, 10);
        insertEnemy(db, 3, "XIII - Death", 44, Enemy.INTENT_ATTACK, 9);
        insertEnemy(db, 4, "XV - The Devil", 48, Enemy.INTENT_BLOCK, 8);
        insertEnemy(db, 5, "IX - The Hermit", 42, Enemy.INTENT_BLOCK, 10);
        insertEnemy(db, 6, "XXI - The World", 70, Enemy.INTENT_ATTACK, 12);
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
}
