package com.fools.game.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fools.game.R;
import com.fools.game.components.AnimationEngine;
import com.fools.game.core.GameManager;
import com.fools.game.models.Card;
import com.fools.game.models.Effect;
import com.fools.game.models.Enemy;
import com.fools.game.models.Entity;
import com.fools.game.models.Player;
import com.fools.game.ui.adapters.CardAdapter;
import com.fools.game.ui.viewmodels.CombatViewModel;

import java.util.ArrayList;
import java.util.List;

public class CombatFragment extends Fragment {

    private CombatViewModel viewModel;
    private CardAdapter cardAdapter;

    private ProgressBar playerHpBar, enemyHpBar;
    private TextView playerHpText, enemyHpText, playerEnergyText, enemyNameText, enemyIntentText;
    private ImageView enemyIcon, enemyIntentIcon;
    private Button endTurnButton;
    private RecyclerView cardsRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_combat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        playerHpBar = view.findViewById(R.id.player_hp_bar);
        enemyHpBar = view.findViewById(R.id.enemy_hp_bar);
        playerHpText = view.findViewById(R.id.player_hp_text);
        enemyHpText = view.findViewById(R.id.enemy_hp_text);
        playerEnergyText = view.findViewById(R.id.player_energy_text);
        enemyNameText = view.findViewById(R.id.enemy_name);
        enemyIcon = view.findViewById(R.id.enemy_icon);
        enemyIntentText = view.findViewById(R.id.enemy_intent_text);
        enemyIntentIcon = view.findViewById(R.id.enemy_intent_icon);
        endTurnButton = view.findViewById(R.id.btn_end_turn);
        cardsRecyclerView = view.findViewById(R.id.cards_recycler_view);

        // Setup RecyclerView
        cardAdapter = new CardAdapter(this::onCardClicked);
        cardsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        cardsRecyclerView.setAdapter(cardAdapter);

        // Setup ViewModel
        viewModel = new ViewModelProvider(this).get(CombatViewModel.class);

        // Start actual game session
        setupCombatSession();

        // Observe Data
        observeViewModel();

        // Setup End Turn Button
        endTurnButton.setOnClickListener(v -> {
            GameManager.getInstance().endPlayerTurn();
        });
    }

    private void observeViewModel() {
        viewModel.getPlayer().observe(getViewLifecycleOwner(), player -> {
            if (player != null) {
                playerHpBar.setMax(player.getMaxHealth());
                playerHpBar.setProgress(player.getCurrentHealth());
                playerHpText.setText(player.getCurrentHealth() + " / " + player.getMaxHealth());
                playerEnergyText.setText("Energy: " + player.getCurrentEnergy() + "/" + player.getMaxEnergy());
                
                if(player.getBlock() > 0) {
                     playerHpText.setText(player.getCurrentHealth() + " (+" + player.getBlock() + " Block) / " + player.getMaxHealth());
                }
            }
        });

        viewModel.getEnemy().observe(getViewLifecycleOwner(), enemy -> {
            if (enemy != null) {
                enemyNameText.setText(enemy.getName());
                enemyIntentText.setText(enemy.getNextIntent());
                enemyHpBar.setMax(enemy.getMaxHealth());
                enemyHpBar.setProgress(enemy.getCurrentHealth());
                enemyHpText.setText(enemy.getCurrentHealth() + " / " + enemy.getMaxHealth());
                
                if(enemy.getBlock() > 0) {
                     enemyHpText.setText(enemy.getCurrentHealth() + " (+" + enemy.getBlock() + " Block) / " + enemy.getMaxHealth());
                }
                
                if(enemy.isDead()) {
                     Toast.makeText(getContext(), "Victory!", Toast.LENGTH_SHORT).show();
                     GameManager.getInstance().advanceEncounter();
                     if (getView() != null) {
                         androidx.navigation.Navigation.findNavController(getView()).navigate(R.id.action_combatFragment_to_rewardFragment);
                     }
                }
            }
        });

        viewModel.getHand().observe(getViewLifecycleOwner(), cards -> {
            if (cards != null) {
                cardAdapter.setCards(cards);
            }
        });
        
        viewModel.getIsPlayerTurn().observe(getViewLifecycleOwner(), isPlayerTurn -> {
            endTurnButton.setEnabled(isPlayerTurn);
        });
    }

    private void onCardClicked(Card card, View cardView) {
        if (Boolean.TRUE.equals(viewModel.getIsPlayerTurn().getValue())) {
            Player p = viewModel.getPlayer().getValue();
            if(p != null && p.getCurrentEnergy() >= card.getCost()) {
                
                // Play animation before applying logic
                AnimationEngine.playCardAnimation(cardView, () -> {
                    GameManager.getInstance().playCard(card);
                    
                    // Shake enemy if attacking
                    if (card.getSuit() == Card.Suit.WANDS) {
                        AnimationEngine.shakeView(enemyIcon);
                    }
                });
                
            } else {
                Toast.makeText(getContext(), "Not enough energy!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupCombatSession() {
        int index = GameManager.getInstance().getCurrentEncounterIndex();
        Enemy boss = com.fools.game.data.BossFactory.createBoss(index);
        GameManager.getInstance().startCombat(boss, viewModel);
    }
}
