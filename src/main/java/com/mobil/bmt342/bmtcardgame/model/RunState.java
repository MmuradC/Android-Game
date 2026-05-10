package com.mobil.bmt342.bmtcardgame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RunState {
    private final PlayerState playerState = new PlayerState(50);
    private final ArrayList<Card> deck = new ArrayList<>();
    private boolean runActive;
    private int currentEncounterId = 1;
    private int furthestUnlockedEncounterId = 1;
    private int runCounter;
    private int discardTokens;

    public PlayerState getPlayerState() {
        return playerState;
    }

    public List<Card> getDeck() {
        return Collections.unmodifiableList(deck);
    }

    public boolean isRunActive() {
        return runActive;
    }

    public int getCurrentEncounterId() {
        return currentEncounterId;
    }

    public int getFurthestUnlockedEncounterId() {
        return furthestUnlockedEncounterId;
    }

    public int getRunCounter() {
        return runCounter;
    }

    public int getDiscardTokens() {
        return discardTokens;
    }

    public boolean isBossEncounter() {
        return currentEncounterId >= 6;
    }

    public void startRun(List<Card> starterCards) {
        deck.clear();
        deck.addAll(starterCards);
        playerState.resetForNewRun();
        currentEncounterId = 1;
        furthestUnlockedEncounterId = 1;
        runCounter = 0;
        discardTokens = 0;
        runActive = true;
    }

    public void loadCampaign(List<Card> savedDeck, int savedRunCounter, int savedFurthestEncounterId,
                             int savedDiscardTokens) {
        deck.clear();
        deck.addAll(savedDeck);
        playerState.resetForNewRun();
        currentEncounterId = Math.max(1, Math.min(6, savedFurthestEncounterId));
        furthestUnlockedEncounterId = Math.max(1, Math.min(6, savedFurthestEncounterId));
        runCounter = Math.max(0, savedRunCounter);
        discardTokens = Math.max(0, savedDiscardTokens);
        runActive = true;
    }

    public void addCard(Card card) {
        deck.add(card);
    }

    public void removeCard(int cardId) {
        for (int i = 0; i < deck.size(); i++) {
            if (deck.get(i).getId() == cardId) {
                deck.remove(i);
                discardTokens = Math.max(0, discardTokens - 1);
                return;
            }
        }
    }

    public void endRun() {
        runActive = false;
    }

    public void recordDefeat() {
        runCounter++;
        playerState.resetForNewRun();
        currentEncounterId = furthestUnlockedEncounterId;
        runActive = true;
    }

    public void selectEncounter(int encounterId) {
        if (encounterId <= furthestUnlockedEncounterId) {
            currentEncounterId = Math.max(1, Math.min(6, encounterId));
        }
    }

    public void advanceEncounter() {
        if (currentEncounterId < 6) {
            int previousFurthest = furthestUnlockedEncounterId;
            furthestUnlockedEncounterId = Math.max(furthestUnlockedEncounterId, currentEncounterId + 1);
            if (furthestUnlockedEncounterId > previousFurthest) {
                discardTokens++;
            }
            currentEncounterId = furthestUnlockedEncounterId;
        } else {
            runActive = false;
        }
    }
}
