package com.mobil.bmt342.bmtcardgame.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RunState {
    private final PlayerState playerState = new PlayerState(50);
    private final ArrayList<Card> deck = new ArrayList<>();
    private boolean runActive;
    private int currentEncounterId = 1;

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

    public boolean isBossEncounter() {
        return currentEncounterId >= 6;
    }

    public void startRun(List<Card> starterCards) {
        deck.clear();
        deck.addAll(starterCards);
        playerState.resetForNewRun();
        currentEncounterId = 1;
        runActive = true;
    }

    public void addCard(Card card) {
        deck.add(card);
    }

    public void endRun() {
        runActive = false;
    }

    public void advanceEncounter() {
        if (currentEncounterId < 6) {
            currentEncounterId++;
        } else {
            runActive = false;
        }
    }
}
