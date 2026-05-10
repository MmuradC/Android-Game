package com.fools.game.data;

import android.content.Context;
import android.widget.Toast;

import com.fools.game.R;
import com.fools.game.components.AnimationEngine;
import com.fools.game.models.Enemy;
import com.fools.game.models.Player;

public class BossFactory {

    public static Enemy createBoss(int encounterIndex) {
        switch (encounterIndex) {
            case 0:
                return new Enemy("The Magician", 60) {
                    private int damage = 5;
                    @Override
                    public void planNextAction() {
                        setNextIntent("Attack for " + damage);
                    }
                    @Override
                    public void performAction(Player player) {
                        player.takeDamage(damage);
                        damage += 1;
                    }
                };
            case 1:
                return new Enemy("The Tower", 120) {
                    @Override
                    public void planNextAction() {
                        setNextIntent("Heavy Attack for 12");
                    }
                    @Override
                    public void performAction(Player player) {
                        player.takeDamage(12);
                    }
                };
            case 2:
                return new Enemy("Death", 80) {
                    @Override
                    public void planNextAction() {
                        setNextIntent("Decay: 3 Dmg + 1 Max HP Loss");
                    }
                    @Override
                    public void performAction(Player player) {
                        player.takeDamage(3);
                        // A true Max HP loss would require expanding Player model, simulate with dmg for now
                    }
                };
            // Add Devil, Hermit, World later
            default:
                return new Enemy("The World", 200) {
                    @Override
                    public void planNextAction() {
                        setNextIntent("Attack for 15");
                    }
                    @Override
                    public void performAction(Player player) {
                        player.takeDamage(15);
                    }
                };
        }
    }

    public static int getBossIcon(int encounterIndex) {
        switch (encounterIndex) {
            case 0: return R.drawable.ic_magician;
            case 1: return R.drawable.ic_tower;
            case 2: return R.drawable.ic_death;
            case 3: return R.drawable.ic_devil;
            case 4: return R.drawable.ic_hermit;
            default: return R.drawable.ic_world;
        }
    }

    public static String getBossName(int encounterIndex) {
        switch (encounterIndex) {
            case 0: return "The Magician";
            case 1: return "The Tower";
            case 2: return "Death";
            case 3: return "The Devil";
            case 4: return "The Hermit";
            default: return "The World";
        }
    }
}
