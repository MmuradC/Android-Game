package com.mobil.bmt342.bmtcardgame;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mobil.bmt342.bmtcardgame.data.repository.CardRepository;
import com.mobil.bmt342.bmtcardgame.databinding.ActivityMainBinding;
import com.mobil.bmt342.bmtcardgame.ui.codex.CodexFragment;
import com.mobil.bmt342.bmtcardgame.ui.combat.CombatFragment;
import com.mobil.bmt342.bmtcardgame.ui.map.MapFragment;
import com.mobil.bmt342.bmtcardgame.ui.menu.MenuFragment;
import com.mobil.bmt342.bmtcardgame.ui.reward.RewardFragment;
import com.mobil.bmt342.bmtcardgame.util.NotificationHelper;
import com.mobil.bmt342.bmtcardgame.viewmodel.RunViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private CardRepository cardRepository;
    private RunViewModel runViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cardRepository = new CardRepository(this);
        runViewModel = new ViewModelProvider(this).get(RunViewModel.class);
        NotificationHelper.ensureChannel(this);
        requestNotificationPermissionIfNeeded();

        if (savedInstanceState == null) {
            showMenu();
        }
    }

    public CardRepository getCardRepository() {
        return cardRepository;
    }

    public RunViewModel getRunViewModel() {
        return runViewModel;
    }

    public void startRun() {
        runViewModel.startRun(cardRepository.getStarterDeck());
        NotificationHelper.showRunStarted(this);
        showMap();
    }

    public void showMenu() {
        navigate(new MenuFragment(), false);
    }

    public void showMap() {
        navigate(new MapFragment(), true);
    }

    public void showCombat() {
        navigate(new CombatFragment(), true);
    }

    public void showReward() {
        navigate(new RewardFragment(), true);
    }

    public void showCodex() {
        navigate(new CodexFragment(), true);
    }

    private void navigate(Fragment fragment, boolean addToBackStack) {
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.commit();
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 20);
        }
    }
}
