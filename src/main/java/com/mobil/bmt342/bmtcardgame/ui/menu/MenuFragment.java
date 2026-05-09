package com.mobil.bmt342.bmtcardgame.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobil.bmt342.bmtcardgame.MainActivity;
import com.mobil.bmt342.bmtcardgame.databinding.FragmentMenuBinding;
import com.mobil.bmt342.bmtcardgame.util.PreferenceManager;

public class MenuFragment extends Fragment {
    private FragmentMenuBinding binding;
    private PreferenceManager preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        preferences = new PreferenceManager(requireContext());
        binding.playerNameInput.setText(preferences.getPlayerName());
        binding.soundSwitch.setChecked(preferences.isSoundEnabled());

        if (!preferences.hasSeenIntro()) {
            Toast.makeText(requireContext(), "Welcome, Fool. Your journey begins.", Toast.LENGTH_SHORT).show();
            preferences.markIntroSeen();
        }

        binding.startRunButton.setOnClickListener(v -> {
            preferences.setPlayerName(binding.playerNameInput.getText().toString());
            preferences.setSoundEnabled(binding.soundSwitch.isChecked());
            ((MainActivity) requireActivity()).startRun();
        });

        binding.codexButton.setOnClickListener(v -> ((MainActivity) requireActivity()).showCodex());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
