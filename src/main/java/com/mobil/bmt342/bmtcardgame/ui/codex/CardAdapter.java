package com.mobil.bmt342.bmtcardgame.ui.codex;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobil.bmt342.bmtcardgame.R;
import com.mobil.bmt342.bmtcardgame.databinding.ItemPlayerCardBinding;
import com.mobil.bmt342.bmtcardgame.model.Card;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    public interface OnCardClickListener {
        void onCardClicked(Card card);
    }

    private final ArrayList<Card> cards = new ArrayList<>();
    private final OnCardClickListener listener;
    private final boolean compact;
    private Card selectedCard;

    public CardAdapter(OnCardClickListener listener) {
        this(listener, false);
    }

    public CardAdapter(OnCardClickListener listener, boolean compact) {
        this.listener = listener;
        this.compact = compact;
    }

    public void submitList(List<Card> newCards) {
        cards.clear();
        cards.addAll(newCards);
        notifyDataSetChanged();
    }

    public void setSelectedCard(Card selectedCard) {
        this.selectedCard = selectedCard;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlayerCardBinding binding = ItemPlayerCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        if (compact) {
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                    (int) (190 * parent.getResources().getDisplayMetrics().density),
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            params.setMargins(6, 6, 6, 6);
            binding.getRoot().setLayoutParams(params);
        }
        return new CardViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(cards.get(position));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        private final ItemPlayerCardBinding binding;

        CardViewHolder(ItemPlayerCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Card card) {
            boolean selected = selectedCard != null && selectedCard == card;
            binding.cardName.setText(card.getName());
            binding.cardFamily.setText(card.getFamily() + " - " + labelForEffect(card));
            binding.cardCost.setText(String.valueOf(card.getCost()));
            binding.cardDescription.setText(card.getDescription());
            binding.familyStrip.setBackgroundResource(colorForFamily(card.getFamily()));
            binding.getRoot().setSelected(selected);
            binding.getRoot().setScaleX(selected ? 1.06f : 1f);
            binding.getRoot().setScaleY(selected ? 1.06f : 1f);
            binding.getRoot().setTranslationY(selected ? -18f : 0f);
            binding.getRoot().setElevation(selected ? 12f : 2f);
            binding.selectionGlow.setVisibility(selected ? android.view.View.VISIBLE : android.view.View.GONE);
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCardClicked(card);
                }
            });
        }

        private int colorForFamily(String family) {
            if ("Cups".equals(family)) {
                return R.color.cups;
            }
            if ("Swords".equals(family)) {
                return R.color.swords;
            }
            if ("Pentacles".equals(family)) {
                return R.color.pentacles;
            }
            return R.color.wands;
        }

        private String labelForEffect(Card card) {
            if (Card.BLOCK.equals(card.getEffectType())) {
                return "Block " + card.getValue();
            }
            if (Card.HEAL.equals(card.getEffectType())) {
                return "Heal " + card.getValue();
            }
            if (Card.LIFESTEAL.equals(card.getEffectType())) {
                return "Drain " + card.getValue();
            }
            if (Card.DRAW.equals(card.getEffectType())) {
                return "Draw " + card.getValue();
            }
            if (Card.ENERGY.equals(card.getEffectType())) {
                return "Energy " + card.getValue();
            }
            return "Damage " + card.getValue();
        }
    }
}
