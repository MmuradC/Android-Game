package com.mobil.bmt342.bmtcardgame.ui.defeat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobil.bmt342.bmtcardgame.MainActivity;
import com.mobil.bmt342.bmtcardgame.databinding.FragmentDefeatBinding;

public class DefeatFragment extends Fragment {
    private FragmentDefeatBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDefeatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) requireActivity();
        int deathCount = activity.getRunViewModel().getRunState().getRunCounter();
        int furthestEncounter = activity.getRunViewModel().getRunState().getFurthestUnlockedEncounterId();
        String enemyName = activity.getCardRepository().getEnemyNameForEncounter(furthestEncounter);

        binding.defeatSubtitle.setText("Death count: " + deathCount
                + ". Unlocked cards remain, and the path is open through " + enemyName + ".");
        binding.returnMapButton.setOnClickListener(v -> activity.showMap());
        binding.menuButton.setOnClickListener(v -> activity.showMenu());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
