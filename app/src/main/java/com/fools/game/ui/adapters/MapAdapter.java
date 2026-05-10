package com.fools.game.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fools.game.R;
import com.fools.game.data.BossFactory;
import com.google.android.material.card.MaterialCardView;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.MapViewHolder> {

    private int currentEncounterIndex;
    private OnNodeClickListener listener;
    private final int TOTAL_ENCOUNTERS = 6;

    public interface OnNodeClickListener {
        void onNodeClick(int index);
    }

    public MapAdapter(int currentEncounterIndex, OnNodeClickListener listener) {
        this.currentEncounterIndex = currentEncounterIndex;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_map_node, parent, false);
        return new MapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MapViewHolder holder, int position) {
        // Reverse so the World is at the top, Magician at the bottom
        int encounterIndex = TOTAL_ENCOUNTERS - 1 - position;
        
        holder.nameText.setText(BossFactory.getBossName(encounterIndex));
        holder.iconImage.setImageResource(BossFactory.getBossIcon(encounterIndex));

        if (encounterIndex < currentEncounterIndex) {
            holder.statusText.setText("Defeated");
            holder.statusText.setTextColor(0xFF888888); // Grey
            holder.cardView.setCardBackgroundColor(0xFF111118); // Darker
            holder.cardView.setStrokeColor(0xFF222233);
            holder.itemView.setOnClickListener(null);
        } else if (encounterIndex == currentEncounterIndex) {
            holder.statusText.setText("Next Encounter");
            holder.statusText.setTextColor(0xFFFFD700); // Gold
            holder.cardView.setCardBackgroundColor(0xFF1A1A24);
            holder.cardView.setStrokeColor(0xFFFFD700); // Gold Highlight
            holder.itemView.setOnClickListener(v -> listener.onNodeClick(encounterIndex));
        } else {
            holder.statusText.setText("Locked");
            holder.statusText.setTextColor(0xFF555555); // Dark Grey
            holder.cardView.setCardBackgroundColor(0xFF111118);
            holder.cardView.setStrokeColor(0xFF222233);
            holder.itemView.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return TOTAL_ENCOUNTERS;
    }

    static class MapViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        ImageView iconImage;
        TextView nameText, statusText;

        public MapViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            iconImage = itemView.findViewById(R.id.node_icon);
            nameText = itemView.findViewById(R.id.node_name);
            statusText = itemView.findViewById(R.id.node_status);
        }
    }
}
