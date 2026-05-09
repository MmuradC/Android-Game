package com.mobil.bmt342.bmtcardgame.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREFS_NAME = "astral_run_prefs";
    private static final String KEY_PLAYER_NAME = "player_name";
    private static final String KEY_SOUND_ENABLED = "sound_enabled";
    private static final String KEY_INTRO_SEEN = "intro_seen";

    private final SharedPreferences preferences;

    public PreferenceManager(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getPlayerName() {
        return preferences.getString(KEY_PLAYER_NAME, "The Fool");
    }

    public void setPlayerName(String name) {
        preferences.edit().putString(KEY_PLAYER_NAME, name.trim().isEmpty() ? "The Fool" : name.trim()).apply();
    }

    public boolean isSoundEnabled() {
        return preferences.getBoolean(KEY_SOUND_ENABLED, true);
    }

    public void setSoundEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply();
    }

    public boolean hasSeenIntro() {
        return preferences.getBoolean(KEY_INTRO_SEEN, false);
    }

    public void markIntroSeen() {
        preferences.edit().putBoolean(KEY_INTRO_SEEN, true).apply();
    }
}
