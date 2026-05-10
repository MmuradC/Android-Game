package com.mobil.bmt342.bmtcardgame.data.repository;

import android.content.ContentValues;
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
        addCardIfInDeck(starter, 1);
        addCardIfInDeck(starter, 2);
        addCardIfInDeck(starter, 3);
        addCardIfInDeck(starter, 5);
        addCardIfInDeck(starter, 7);
        starter.removeIf(card -> card == null);
        return starter;
    }

    public List<Card> getCampaignDeck() {
        ArrayList<Card> deck = new ArrayList<>(getStarterDeck());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(GameDatabaseHelper.TABLE_UNLOCKED_CARDS, null,
                null, null, null, null, "card_id ASC");
        while (cursor.moveToNext()) {
            addCardIfInDeck(deck, cursor.getInt(cursor.getColumnIndexOrThrow("card_id")));
        }
        cursor.close();
        return deck;
    }

    public List<Card> getRewardChoices() {
        return getAvailableRewardChoices(1);
    }

    public List<Card> getAvailableRewardChoices(int encounterId) {
        ArrayList<Card> choices = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(GameDatabaseHelper.TABLE_ENCOUNTER_REWARDS, null,
                "encounter_id = ?", new String[]{String.valueOf(encounterId)},
                null, null, "card_id ASC");
        while (cursor.moveToNext()) {
            int cardId = cursor.getInt(cursor.getColumnIndexOrThrow("card_id"));
            if (!isRewardClaimed(encounterId, cardId) && !isCardUnlocked(cardId)) {
                choices.add(getCardById(cardId));
            }
        }
        cursor.close();
        choices.removeIf(card -> card == null);
        return choices;
    }

    public boolean hasSavedGame() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(GameDatabaseHelper.TABLE_GAME_PROGRESS, null,
                "id = 1", null, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public void startNewGame() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(GameDatabaseHelper.TABLE_UNLOCKED_CARDS, null, null);
        db.delete(GameDatabaseHelper.TABLE_CLAIMED_REWARDS, null, null);
        db.delete(GameDatabaseHelper.TABLE_REMOVED_CARDS, null, null);
        db.delete(GameDatabaseHelper.TABLE_GAME_PROGRESS, null, null);
        ContentValues values = new ContentValues();
        values.put("id", 1);
        values.put("run_counter", 0);
        values.put("furthest_unlocked_encounter", 1);
        values.put("discard_tokens", 0);
        db.insert(GameDatabaseHelper.TABLE_GAME_PROGRESS, null, values);
    }

    public void unlockRewardCard(int encounterId, int cardId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues unlocked = new ContentValues();
        unlocked.put("card_id", cardId);
        db.insertWithOnConflict(GameDatabaseHelper.TABLE_UNLOCKED_CARDS, null,
                unlocked, SQLiteDatabase.CONFLICT_IGNORE);

        ContentValues claimed = new ContentValues();
        claimed.put("encounter_id", encounterId);
        claimed.put("card_id", cardId);
        db.insertWithOnConflict(GameDatabaseHelper.TABLE_CLAIMED_REWARDS, null,
                claimed, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public int getRunCounter() {
        return getProgressInt("run_counter", 0);
    }

    public int getFurthestUnlockedEncounter() {
        return getProgressInt("furthest_unlocked_encounter", 1);
    }

    public int getDiscardTokens() {
        return getProgressInt("discard_tokens", 0);
    }

    public void incrementRunCounter() {
        ensureProgressExists();
        ContentValues values = new ContentValues();
        values.put("run_counter", getRunCounter() + 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(GameDatabaseHelper.TABLE_GAME_PROGRESS, values, "id = 1", null);
    }

    public void unlockEncounter(int encounterId) {
        ensureProgressExists();
        int boundedEncounterId = Math.max(1, Math.min(6, encounterId));
        if (boundedEncounterId <= getFurthestUnlockedEncounter()) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put("furthest_unlocked_encounter", boundedEncounterId);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(GameDatabaseHelper.TABLE_GAME_PROGRESS, values, "id = 1", null);
    }

    public void addDiscardToken() {
        ensureProgressExists();
        ContentValues values = new ContentValues();
        values.put("discard_tokens", getDiscardTokens() + 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(GameDatabaseHelper.TABLE_GAME_PROGRESS, values, "id = 1", null);
    }

    public boolean removeCardFromDeck(int cardId) {
        ensureProgressExists();
        if (getDiscardTokens() <= 0 || getCampaignDeck().size() <= 1 || isCardRemoved(cardId)) {
            return false;
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues removed = new ContentValues();
        removed.put("card_id", cardId);
        db.insertWithOnConflict(GameDatabaseHelper.TABLE_REMOVED_CARDS, null,
                removed, SQLiteDatabase.CONFLICT_IGNORE);

        ContentValues progress = new ContentValues();
        progress.put("discard_tokens", getDiscardTokens() - 1);
        db.update(GameDatabaseHelper.TABLE_GAME_PROGRESS, progress, "id = 1", null);
        return true;
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

    private int getProgressInt(String column, int fallback) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(GameDatabaseHelper.TABLE_GAME_PROGRESS, null,
                "id = 1", null, null, null, null);
        int value = fallback;
        if (cursor.moveToFirst()) {
            value = cursor.getInt(cursor.getColumnIndexOrThrow(column));
        }
        cursor.close();
        return value;
    }

    private void addCardIfInDeck(ArrayList<Card> deck, int cardId) {
        if (isCardRemoved(cardId)) {
            return;
        }
        Card card = getCardById(cardId);
        if (card != null) {
            deck.add(card);
        }
    }

    private void ensureProgressExists() {
        if (!hasSavedGame()) {
            startNewGame();
        }
    }

    private boolean isCardUnlocked(int cardId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(GameDatabaseHelper.TABLE_UNLOCKED_CARDS, null,
                "card_id = ?", new String[]{String.valueOf(cardId)},
                null, null, null);
        boolean unlocked = cursor.moveToFirst();
        cursor.close();
        return unlocked;
    }

    private boolean isRewardClaimed(int encounterId, int cardId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(GameDatabaseHelper.TABLE_CLAIMED_REWARDS, null,
                "encounter_id = ? AND card_id = ?",
                new String[]{String.valueOf(encounterId), String.valueOf(cardId)},
                null, null, null);
        boolean claimed = cursor.moveToFirst();
        cursor.close();
        return claimed;
    }

    private boolean isCardRemoved(int cardId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(GameDatabaseHelper.TABLE_REMOVED_CARDS, null,
                "card_id = ?", new String[]{String.valueOf(cardId)},
                null, null, null);
        boolean removed = cursor.moveToFirst();
        cursor.close();
        return removed;
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
