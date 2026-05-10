package com.fools.game.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fools.game.models.Card;
import com.fools.game.models.Enemy;
import com.fools.game.models.Player;

import java.util.ArrayList;
import java.util.List;

public class CombatViewModel extends ViewModel {
    private MutableLiveData<Player> playerLiveData = new MutableLiveData<>();
    private MutableLiveData<Enemy> enemyLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Card>> handLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isPlayerTurn = new MutableLiveData<>(true);

    public void initialize(Player player, Enemy enemy) {
        playerLiveData.setValue(player);
        enemyLiveData.setValue(enemy);
        if (player.getDeck() != null) {
            handLiveData.setValue(player.getDeck().getHand());
        } else {
            handLiveData.setValue(new ArrayList<>());
        }
    }

    public LiveData<Player> getPlayer() { return playerLiveData; }
    public LiveData<Enemy> getEnemy() { return enemyLiveData; }
    public LiveData<List<Card>> getHand() { return handLiveData; }
    public LiveData<Boolean> getIsPlayerTurn() { return isPlayerTurn; }

    public void setHand(List<Card> cards) {
        handLiveData.setValue(cards);
    }

    public void updateState() {
        Player p = playerLiveData.getValue();
        playerLiveData.setValue(p);
        enemyLiveData.setValue(enemyLiveData.getValue());
        if (p != null && p.getDeck() != null) {
            handLiveData.setValue(p.getDeck().getHand());
        }
    }
    
    public void endPlayerTurn() {
        isPlayerTurn.setValue(false);
    }
    
    public void startPlayerTurn() {
        isPlayerTurn.setValue(true);
    }
}
