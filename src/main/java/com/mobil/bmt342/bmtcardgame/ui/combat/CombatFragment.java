package com.mobil.bmt342.bmtcardgame.ui.combat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mobil.bmt342.bmtcardgame.MainActivity;
import com.mobil.bmt342.bmtcardgame.R;
import com.mobil.bmt342.bmtcardgame.databinding.FragmentCombatBinding;
import com.mobil.bmt342.bmtcardgame.model.Enemy;
import com.mobil.bmt342.bmtcardgame.model.PlayerState;
import com.mobil.bmt342.bmtcardgame.ui.codex.CardAdapter;
import com.mobil.bmt342.bmtcardgame.viewmodel.CombatViewModel;

public class CombatFragment extends Fragment {
    private FragmentCombatBinding binding;
    private CombatViewModel combatViewModel;
    private CardAdapter handAdapter;
    private boolean terminalResultHandled;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCombatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) requireActivity();
        combatViewModel = new ViewModelProvider(this).get(CombatViewModel.class);

        handAdapter = new CardAdapter(card -> {
            combatViewModel.selectCard(card);
            updateUi();
        }, true);
        binding.handRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.handRecycler.setAdapter(handAdapter);

        if (combatViewModel.getEnemy() == null) {
            combatViewModel.startCombat(
                    activity.getRunViewModel().getRunState().getPlayerState(),
                    activity.getCardRepository().getEnemyForEncounter(
                            activity.getRunViewModel().getRunState().getCurrentEncounterId()
                    ),
                    activity.getRunViewModel().getRunState().getDeck()
            );
        }

        binding.endTurnButton.setOnClickListener(v -> {
            if (terminalResultHandled) {
                return;
            }
            CombatViewModel.PlayResult result = combatViewModel.endTurn();
            updateUi();
            handleResult(result);
        });

        binding.playCardButton.setOnClickListener(v -> {
            if (terminalResultHandled) {
                return;
            }
            if (combatViewModel.getSelectedCard() == null) {
                Toast.makeText(requireContext(), "Select a card first.", Toast.LENGTH_SHORT).show();
                return;
            }
            CombatViewModel.PlayResult result = combatViewModel.playSelectedCard();
            updateUi();
            handleResult(result);
        });

        updateUi();
    }

    private void updateUi() {
        PlayerState player = combatViewModel.getPlayerState();
        Enemy enemy = combatViewModel.getEnemy();
        if (player == null || enemy == null || binding == null) {
            return;
        }

        binding.playerHpText.setText("HP: " + player.getCurrentHp() + "/" + player.getMaxHp());
        binding.playerHpBar.setMax(player.getMaxHp());
        binding.playerHpBar.setProgress(player.getCurrentHp());
        binding.playerBlockText.setText("Block: " + player.getBlock());
        updateEnergyUI(player.getEnergy());
        binding.enemyCardFrame.enemyMajorNumber.setText(enemy.getMajorNumber());
        binding.enemyCardFrame.enemyName.setText(enemy.getDisplayName());
        binding.enemyCardFrame.enemyStats.setText("Enemy HP: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp()
                + "  Block: " + enemy.getBlock());
        binding.enemyCardFrame.enemyHpBar.setMax(enemy.getMaxHp());
        binding.enemyCardFrame.enemyHpBar.setProgress(enemy.getCurrentHp());
        binding.enemyCardFrame.enemyIntent.setText(enemy.getIntentText());
        binding.enemyCardFrame.enemyIcon.setImageResource(iconForEnemy(enemy));
        handAdapter.submitList(combatViewModel.getHand());
        handAdapter.setSelectedCard(combatViewModel.getSelectedCard());
        if (combatViewModel.getSelectedCard() == null) {
            binding.selectedCardText.setText("Select a card");
            binding.playCardButton.setEnabled(false);
            binding.playCardButton.setAlpha(0.55f);
        } else {
            binding.selectedCardText.setText("Selected: " + combatViewModel.getSelectedCard().getName());
            binding.playCardButton.setEnabled(true);
            binding.playCardButton.setAlpha(1f);
        }
    }

    private void updateEnergyUI(int currentEnergy) {
        int energy = Math.max(0, Math.min(3, currentEnergy));
        binding.energyPip1.setImageResource(energy >= 1
                ? R.drawable.bar_square_gloss_small_square
                : R.drawable.bar_square_gloss_small_square_grey);
        binding.energyPip2.setImageResource(energy >= 2
                ? R.drawable.bar_square_gloss_small_square
                : R.drawable.bar_square_gloss_small_square_grey);
        binding.energyPip3.setImageResource(energy >= 3
                ? R.drawable.bar_square_gloss_small_square
                : R.drawable.bar_square_gloss_small_square_grey);
    }

    private int iconForEnemy(Enemy enemy) {
        switch (enemy.getId()) {
            case 2:
                return R.drawable.ic_tower;
            case 3:
                return R.drawable.ic_death;
            case 4:
                return R.drawable.ic_devil;
            case 5:
                return R.drawable.ic_hermit;
            case 6:
                return R.drawable.ic_world;
            case 1:
            default:
                return R.drawable.ic_magician;
        }
    }

    private void handleResult(CombatViewModel.PlayResult result) {
        MainActivity activity = (MainActivity) requireActivity();
        if (result == CombatViewModel.PlayResult.NOT_ENOUGH_ENERGY) {
            showCombatMessage("Not enough energy.");
        } else if (result == CombatViewModel.PlayResult.VICTORY) {
            if (terminalResultHandled) {
                return;
            }
            terminalResultHandled = true;
            setCombatButtonsEnabled(false);
            showCombatMessage("Victory! Choose a card.");
            binding.getRoot().postDelayed(activity::showReward, 1200);
        } else if (result == CombatViewModel.PlayResult.DEFEAT) {
            if (terminalResultHandled) {
                return;
            }
            terminalResultHandled = true;
            setCombatButtonsEnabled(false);
            showCombatMessage("Run ended in the void.");
            binding.getRoot().postDelayed(activity::recordDefeat, 1200);
        }
    }

    private void setCombatButtonsEnabled(boolean enabled) {
        binding.playCardButton.setEnabled(enabled);
        binding.endTurnButton.setEnabled(enabled);
        binding.playCardButton.setAlpha(enabled ? 1f : 0.45f);
        binding.endTurnButton.setAlpha(enabled ? 1f : 0.45f);
    }

    private void showCombatMessage(String message) {
        binding.selectedCardText.setText(message);
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
