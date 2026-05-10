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
import com.mobil.bmt342.bmtcardgame.model.Card;
import com.mobil.bmt342.bmtcardgame.ui.codex.CardAdapter;

import java.util.List;

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
            activity.claimRewardAndContinue(card);
            Toast.makeText(requireContext(), card.getName() + " added to deck.", Toast.LENGTH_SHORT).show();
        });
        binding.rewardRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rewardRecycler.setAdapter(adapter);
        int encounterId = activity.getRunViewModel().getRunState().getCurrentEncounterId();
        List<Card> choices = activity.getCardRepository().getAvailableRewardChoices(encounterId);
        adapter.submitList(choices);

        if (choices.isEmpty()) {
            binding.rewardSubtitle.setText("No unclaimed cards remain for this encounter.");
            binding.rewardRecycler.setVisibility(View.GONE);
            binding.continueButton.setVisibility(View.VISIBLE);
        } else {
            binding.rewardSubtitle.setText("Pick 1 card. The other choices stay available if you replay this encounter.");
            binding.rewardRecycler.setVisibility(View.VISIBLE);
            binding.continueButton.setVisibility(View.GONE);
        }

        binding.continueButton.setOnClickListener(v -> activity.completeEncounterAndContinue());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
