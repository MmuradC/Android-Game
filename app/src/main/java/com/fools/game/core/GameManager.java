package com.fools.game.core;

import com.fools.game.data.CardRepository;
import com.fools.game.models.Card;
import com.fools.game.models.Deck;
import com.fools.game.models.Enemy;
import com.fools.game.models.Player;
import com.fools.game.ui.viewmodels.CombatViewModel;

public class GameManager {
    private static GameManager instance;
    private Player player;
    private Enemy currentEnemy;
    private CombatViewModel viewModel;
    private int currentEncounterIndex = 0;

    private GameManager() {
        // Initialize player once per run
        player = new Player("The Fool", 50, 3);
        player.setDeck(new Deck(CardRepository.getStartingDeck()));
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public Player getPlayer() { return player; }
    public int getCurrentEncounterIndex() { return currentEncounterIndex; }
    
    public void advanceEncounter() {
        currentEncounterIndex++;
    }

    public void startCombat(Enemy enemy, CombatViewModel viewModel) {
        this.currentEnemy = enemy;
        this.viewModel = viewModel;
        
        // Reset player energy/block for new combat, deck state is preserved
        this.player.restoreEnergy();
        this.player.resetBlock();
        
        this.currentEnemy.planNextAction();
        this.viewModel.initialize(player, enemy);
        startPlayerTurn();
    }

    public void startPlayerTurn() {
        player.restoreEnergy();
        player.resetBlock();
        player.drawCards(5);
        
        viewModel.startPlayerTurn();
        viewModel.updateState();
    }

    public void playCard(Card card) {
        if (Boolean.TRUE.equals(viewModel.getIsPlayerTurn().getValue())) {
            if (player.spendEnergy(card.getCost())) {
                player.getDeck().discardCard(card);
                card.play(player, currentEnemy);
                
                viewModel.updateState();
                checkCombatEnd();
            }
        }
    }

    public void endPlayerTurn() {
        viewModel.endPlayerTurn();
        player.getDeck().discardHand();
        currentEnemy.resetBlock();
        
        // Enemy takes its turn
        currentEnemy.performAction(player);
        
        if (!checkCombatEnd()) {
            currentEnemy.planNextAction();
            viewModel.updateState();
            startPlayerTurn();
        } else {
            viewModel.updateState();
        }
    }

    private boolean checkCombatEnd() {
        if (currentEnemy.isDead()) {
            return true;
        } else if (player.isDead()) {
            return true;
        }
        return false;
    }
}

