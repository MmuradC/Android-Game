package com.mobil.bmt342.bmtcardgame.ui.map;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobil.bmt342.bmtcardgame.MainActivity;
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
        if (activity.getRunViewModel().getRunState().isRunActive()) {
            String enemyName = activity.getCardRepository().getEnemyNameForEncounter(
                    activity.getRunViewModel().getRunState().getCurrentEncounterId()
            );
            binding.mapSubtitle.setText("The Fool's path continues. Next Major Arcana: " + enemyName + ".");
            binding.combatNodeButton.setText(enemyName);
        } else {
            binding.mapSubtitle.setText("The Fool has completed this path. Start a new journey from the menu.");
            binding.combatNodeButton.setText("Journey Complete");
            binding.combatNodeButton.setEnabled(false);
        }
        binding.combatNodeButton.setOnClickListener(v -> activity.showCombat());
        binding.codexButton.setOnClickListener(v -> activity.showCodex());
        binding.quitRunButton.setOnClickListener(v -> showQuitDialog(activity));
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
