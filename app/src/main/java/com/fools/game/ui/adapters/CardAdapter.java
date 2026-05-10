package com.fools.game.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fools.game.R;
import com.fools.game.models.Card;
import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<Card> cards = new ArrayList<>();
    private OnCardClickListener listener;

    public interface OnCardClickListener {
        void onCardClick(Card card, View view);
    }

    public CardAdapter(OnCardClickListener listener) {
        this.listener = listener;
    }

    public void setCards(List<Card> newCards) {
        this.cards.clear();
        this.cards.addAll(newCards);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.bind(card);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCardClick(card, holder.itemView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, costText, suitText, descriptionText;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.card_name);
            costText = itemView.findViewById(R.id.card_cost);
            suitText = itemView.findViewById(R.id.card_suit);
            descriptionText = itemView.findViewById(R.id.card_description);
        }

        public void bind(Card card) {
            nameText.setText(card.getName());
            costText.setText(String.valueOf(card.getCost()));
            suitText.setText(card.getSuit().name());
            descriptionText.setText(card.getDescription());

            // Color-code the suit text based on element
            switch (card.getSuit()) {
                case WANDS:
                    suitText.setTextColor(0xFFFF5252); // Red
                    break;
                case CUPS:
                    suitText.setTextColor(0xFF448AFF); // Blue
                    break;
                case PENTACLES:
                    suitText.setTextColor(0xFFFFD740); // Yellow
                    break;
                case SWORDS:
                    suitText.setTextColor(0xFFE0E0E0); // White/Grey
                    break;
            }
        }
    }
}
