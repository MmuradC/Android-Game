package com.mobil.bmt342.bmtcardgame.ui.reward;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mobil.bmt342.bmtcardgame.MainActivity;
import com.mobil.bmt342.bmtcardgame.databinding.FragmentRewardBinding;
import com.mobil.bmt342.bmtcardgame.ui.codex.CardAdapter;

public class RewardFragment extends Fragment {
    private FragmentRewardBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRewardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) requireActivity();
        CardAdapter adapter = new CardAdapter(card -> {
            activity.getRunViewModel().addCardToDeck(card);
            activity.getRunViewModel().advanceEncounter();
            Toast.makeText(requireContext(), card.getName() + " added to deck.", Toast.LENGTH_SHORT).show();
            activity.showMap();
        });
        binding.rewardRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rewardRecycler.setAdapter(adapter);
        adapter.submitList(activity.getCardRepository().getRewardChoices());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
