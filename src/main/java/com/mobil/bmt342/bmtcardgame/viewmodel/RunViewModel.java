package com.mobil.bmt342.bmtcardgame.viewmodel;

import androidx.lifecycle.ViewModel;

import com.mobil.bmt342.bmtcardgame.model.Card;
import com.mobil.bmt342.bmtcardgame.model.RunState;

import java.util.List;

public class RunViewModel extends ViewModel {
    private final RunState runState = new RunState();

    public RunState getRunState() {
        return runState;
    }

    public void startRun(List<Card> starterDeck) {
        runState.startRun(starterDeck);
    }

    public void addCardToDeck(Card card) {
        runState.addCard(card);
    }

    public void advanceEncounter() {
        runState.advanceEncounter();
    }

    public void quitRun() {
        runState.endRun();
    }
}
