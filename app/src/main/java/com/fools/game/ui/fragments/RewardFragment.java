package com.fools.game.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fools.game.R;
import com.fools.game.core.GameManager;
import com.fools.game.data.CardRepository;
import com.fools.game.models.Card;
import com.fools.game.ui.adapters.CardAdapter;

import java.util.List;

public class RewardFragment extends Fragment {

    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private Button skipButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reward, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.reward_recycler_view);
        skipButton = view.findViewById(R.id.btn_skip_reward);

        // Generate 3 random cards for the draft
        List<Card> rewardChoices = CardRepository.getRandomRewards(3);

        adapter = new CardAdapter((card, cardView) -> {
            // When a card is picked, add it to the deck and go to the Map
            GameManager.getInstance().getPlayer().getDeck().addCardToDeck(card);
            returnToMap(view);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setCards(rewardChoices);

        skipButton.setOnClickListener(v -> returnToMap(view));
    }

    private void returnToMap(View view) {
        Navigation.findNavController(view).navigate(R.id.action_rewardFragment_to_mapFragment);
    }
}
