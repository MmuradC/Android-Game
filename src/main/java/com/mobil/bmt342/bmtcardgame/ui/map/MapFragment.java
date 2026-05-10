package com.mobil.bmt342.bmtcardgame.ui.map;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.mobil.bmt342.bmtcardgame.MainActivity;
import com.mobil.bmt342.bmtcardgame.R;
import com.mobil.bmt342.bmtcardgame.databinding.FragmentMapBinding;

public class MapFragment extends Fragment {
    private FragmentMapBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) requireActivity();
        configureMapNodes(activity);
        binding.codexButton.setOnClickListener(v -> activity.showCodex());
        binding.deckButton.setText("Deck (" + activity.getRunViewModel().getRunState().getDeck().size()
                + ") - Removes: " + activity.getRunViewModel().getRunState().getDiscardTokens());
        binding.deckButton.setOnClickListener(v -> activity.showDeck());
        binding.quitRunButton.setOnClickListener(v -> showQuitDialog(activity));
    }

    private void configureMapNodes(MainActivity activity) {
        TextView[] nodes = {
                binding.combatNodeButton,
                binding.mapNodeTower,
                binding.mapNodeDeath,
                binding.mapNodeDevil,
                binding.mapNodeHermit,
                binding.mapNodeWorld
        };

        int furthestUnlockedEncounterId = activity.getRunViewModel().getRunState().getFurthestUnlockedEncounterId();
        if (activity.getRunViewModel().getRunState().isRunActive()) {
            String enemyName = activity.getCardRepository().getEnemyNameForEncounter(
                    furthestUnlockedEncounterId
            );
            binding.mapSubtitle.setText("Deaths: " + activity.getRunViewModel().getRunState().getRunCounter()
                    + ". Farthest path: " + enemyName + ".");
        } else {
            binding.mapSubtitle.setText("The Fool has completed this path. Start a new journey from the menu.");
        }

        for (int index = 0; index < nodes.length; index++) {
            int encounterId = index + 1;
            TextView node = nodes[index];
            String enemyName = activity.getCardRepository().getEnemyNameForEncounter(encounterId);
            node.setText(enemyName);
            node.setOnClickListener(null);
            node.setClickable(false);
            node.setEnabled(activity.getRunViewModel().getRunState().isRunActive());

            if (!activity.getRunViewModel().getRunState().isRunActive()) {
                node.setText("Cleared: " + enemyName);
                node.setAlpha(0.7f);
                node.setBackgroundResource(R.drawable.ui_button_plain);
                node.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            } else if (encounterId < furthestUnlockedEncounterId) {
                node.setText("Replay: " + enemyName);
                node.setAlpha(0.85f);
                node.setClickable(true);
                node.setBackgroundResource(R.drawable.ui_button_plain);
                node.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                node.setOnClickListener(v -> activity.selectEncounterAndStart(encounterId));
            } else if (encounterId == furthestUnlockedEncounterId) {
                node.setText("Challenge: " + enemyName);
                node.setAlpha(1f);
                node.setClickable(true);
                node.setBackgroundResource(R.drawable.ui_button_plain);
                node.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                node.setOnClickListener(v -> activity.selectEncounterAndStart(encounterId));
            } else {
                node.setText("Locked: " + enemyName);
                node.setAlpha(0.45f);
                node.setBackgroundResource(R.drawable.panel_background);
                node.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_primary));
            }
        }
    }

    private void showQuitDialog(MainActivity activity) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Quit run?")
                .setMessage("The Fool will leave the current journey behind.")
                .setPositiveButton("Quit", (dialog, which) -> {
                    activity.getRunViewModel().quitRun();
                    activity.showMenu();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
