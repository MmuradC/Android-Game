package com.fools.game.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fools.game.R;
import com.fools.game.core.GameManager;
import com.fools.game.ui.adapters.MapAdapter;

public class MapFragment extends Fragment {

    private RecyclerView recyclerView;
    private MapAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        recyclerView = view.findViewById(R.id.map_recycler_view);
        
        int currentIndex = GameManager.getInstance().getCurrentEncounterIndex();
        adapter = new MapAdapter(currentIndex, index -> {
            Navigation.findNavController(view).navigate(R.id.action_mapFragment_to_combatFragment);
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        
        // Scroll to the current node
        int scrollPosition = Math.max(0, 5 - currentIndex);
        recyclerView.scrollToPosition(scrollPosition);
    }
}
