package com.mobil.bmt342.bmtcardgame.data.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobil.bmt342.bmtcardgame.data.local.GameDatabaseHelper;
import com.mobil.bmt342.bmtcardgame.model.Card;
import com.mobil.bmt342.bmtcardgame.model.Enemy;

import java.util.ArrayList;
import java.util.List;

public class CardRepository {
    private final GameDatabaseHelper helper;

    public CardRepository(Context context) {
        helper = new GameDatabaseHelper(context.getApplicationContext());
    }

    public List<Card> getAllCards() {
        ArrayList<Card> cards = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(GameDatabaseHelper.TABLE_CARDS, null, null, null, null, null, "id ASC");
        while (cursor.moveToNext()) {
            cards.add(cardFromCursor(cursor));
        }
        cursor.close();
        return cards;
    }

    public List<Card> getStarterDeck() {
        ArrayList<Card> starter = new ArrayList<>();
        starter.add(getCardById(1));
        starter.add(getCardById(2));
        starter.add(getCardById(4));
        starter.add(getCardById(10));
        starter.add(getCardById(11));
        starter.removeIf(card -> card == null);
        return starter;
    }

    public List<Card> getRewardChoices() {
        ArrayList<Card> choices = new ArrayList<>();
        choices.add(getCardById(3));
        choices.add(getCardById(5));
        choices.add(getCardById(8));
        choices.removeIf(card -> card == null);
        return choices;
    }

    public Enemy getEnemyForEncounter(int encounterId) {
        Enemy enemy = getEnemyById(encounterId);
        return enemy != null ? enemy : new Enemy(1, "I - The Magician", 32, Enemy.INTENT_ATTACK, 7);
    }

    public String getEnemyNameForEncounter(int encounterId) {
        Enemy enemy = getEnemyById(encounterId);
        return enemy != null ? enemy.getName() : "I - The Magician";
    }

    private Card getCardById(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(GameDatabaseHelper.TABLE_CARDS, null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        Card card = null;
        if (cursor.moveToFirst()) {
            card = cardFromCursor(cursor);
        }
        cursor.close();
        return card;
    }

    private Enemy getEnemyById(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(GameDatabaseHelper.TABLE_ENEMIES, null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        Enemy enemy = null;
        if (cursor.moveToFirst()) {
            enemy = new Enemy(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("max_hp")),
                    cursor.getString(cursor.getColumnIndexOrThrow("intent_type")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("intent_value"))
            );
        }
        cursor.close();
        return enemy;
    }

    private Card cardFromCursor(Cursor cursor) {
        return new Card(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("family")),
                cursor.getInt(cursor.getColumnIndexOrThrow("cost")),
                cursor.getString(cursor.getColumnIndexOrThrow("effect_type")),
                cursor.getInt(cursor.getColumnIndexOrThrow("effect_value")),
                cursor.getString(cursor.getColumnIndexOrThrow("description"))
        );
    }
}
