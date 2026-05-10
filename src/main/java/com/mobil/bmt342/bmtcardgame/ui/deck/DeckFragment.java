package com.mobil.bmt342.bmtcardgame.ui.deck;

import android.app.AlertDialog;
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
import com.mobil.bmt342.bmtcardgame.databinding.FragmentDeckBinding;
import com.mobil.bmt342.bmtcardgame.model.Card;
import com.mobil.bmt342.bmtcardgame.ui.codex.CardAdapter;

public class DeckFragment extends Fragment {
    private FragmentDeckBinding binding;
    private CardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDeckBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainActivity activity = (MainActivity) requireActivity();
        adapter = new CardAdapter(card -> showRemoveDialog(activity, card));
        binding.deckRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.deckRecycler.setAdapter(adapter);
        binding.backButton.setOnClickListener(v -> activity.showMap());
        updateDeckUi(activity);
    }

    private void updateDeckUi(MainActivity activity) {
        int tokens = activity.getRunViewModel().getRunState().getDiscardTokens();
        binding.deckSubtitle.setText("Cards: " + activity.getRunViewModel().getRunState().getDeck().size()
                + "   Removes available: " + tokens);
        adapter.submitList(activity.getRunViewModel().getRunState().getDeck());
    }

    private void showRemoveDialog(MainActivity activity, Card card) {
        if (activity.getRunViewModel().getRunState().getDiscardTokens() <= 0) {
            Toast.makeText(requireContext(), "No deck removals available.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (activity.getRunViewModel().getRunState().getDeck().size() <= 1) {
            Toast.makeText(requireContext(), "The Fool must keep at least one card.", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Remove card?")
                .setMessage(card.getName() + " will be removed from this saved game deck.")
                .setPositiveButton("Remove", (dialog, which) -> {
                    if (activity.removeCardFromDeck(card)) {
                        Toast.makeText(requireContext(), card.getName() + " removed.", Toast.LENGTH_SHORT).show();
                        updateDeckUi(activity);
                    } else {
                        Toast.makeText(requireContext(), "Could not remove card.", Toast.LENGTH_SHORT).show();
                    }
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
