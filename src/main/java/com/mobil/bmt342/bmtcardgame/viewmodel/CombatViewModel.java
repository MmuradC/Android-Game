package com.mobil.bmt342.bmtcardgame.viewmodel;

import androidx.lifecycle.ViewModel;

import com.mobil.bmt342.bmtcardgame.model.Card;
import com.mobil.bmt342.bmtcardgame.model.Enemy;
import com.mobil.bmt342.bmtcardgame.model.PlayerState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CombatViewModel extends ViewModel {
    public enum PlayResult {
        PLAYED,
        NOT_ENOUGH_ENERGY,
        VICTORY,
        DEFEAT
    }

    private final ArrayList<Card> drawPile = new ArrayList<>();
    private final ArrayList<Card> discardPile = new ArrayList<>();
    private final ArrayList<Card> hand = new ArrayList<>();
    private PlayerState playerState;
    private Enemy enemy;
    private Card selectedCard;

    public void startCombat(PlayerState playerState, Enemy enemy, List<Card> deck) {
        this.playerState = playerState;
        this.enemy = enemy;
        drawPile.clear();
        discardPile.clear();
        hand.clear();
        selectedCard = null;
        drawPile.addAll(deck);
        Collections.shuffle(drawPile);
        playerState.resetTurn();
        drawHand();
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    public void selectCard(Card card) {
        selectedCard = card;
    }

    public PlayResult playSelectedCard() {
        if (selectedCard == null || !hand.contains(selectedCard)) {
            return PlayResult.PLAYED;
        }
        return playCard(selectedCard);
    }

    private PlayResult playCard(Card card) {
        if (playerState == null || enemy == null) {
            return PlayResult.DEFEAT;
        }
        if (card.getCost() > playerState.getEnergy()) {
            return PlayResult.NOT_ENOUGH_ENERGY;
        }

        playerState.spendEnergy(card.getCost());
        applyCard(card);
        hand.remove(card);
        discardPile.add(card);
        selectedCard = null;

        if (enemy.getCurrentHp() <= 0) {
            return PlayResult.VICTORY;
        }
        if (playerState.getCurrentHp() <= 0) {
            return PlayResult.DEFEAT;
        }
        return PlayResult.PLAYED;
    }

    public PlayResult endTurn() {
        if (Enemy.INTENT_BLOCK.equals(enemy.getIntentType())) {
            enemy.addBlock(enemy.getIntentValue());
        } else {
            playerState.takeDamage(enemy.getIntentValue());
        }

        if (playerState.getCurrentHp() <= 0) {
            return PlayResult.DEFEAT;
        }

        discardPile.addAll(hand);
        hand.clear();
        selectedCard = null;
        playerState.resetTurn();
        enemy.advanceIntent();
        drawHand();

        return PlayResult.PLAYED;
    }

    private void applyCard(Card card) {
        if (Card.BLOCK.equals(card.getEffectType())) {
            playerState.addBlock(card.getValue());
        } else if (Card.HEAL.equals(card.getEffectType())) {
            playerState.heal(card.getValue());
        } else if (Card.LIFESTEAL.equals(card.getEffectType())) {
            enemy.takeDamage(card.getValue());
            playerState.heal(card.getValue());
        } else if (Card.DRAW.equals(card.getEffectType())) {
            drawCards(card.getValue());
        } else if (Card.ENERGY.equals(card.getEffectType())) {
            playerState.gainEnergy(card.getValue());
        } else {
            enemy.takeDamage(card.getValue());
        }
    }

    private void drawCards(int amount) {
        int targetSize = hand.size() + Math.max(0, amount);
        while (hand.size() < targetSize && (!drawPile.isEmpty() || !discardPile.isEmpty())) {
            if (drawPile.isEmpty()) {
                drawPile.addAll(discardPile);
                discardPile.clear();
                Collections.shuffle(drawPile);
            }
            hand.add(drawPile.remove(0));
        }
    }

    private void drawHand() {
        drawCards(5 - hand.size());
    }
}
