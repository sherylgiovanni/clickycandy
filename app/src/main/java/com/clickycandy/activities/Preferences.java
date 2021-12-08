package com.clickycandy.activities;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import com.clickycandy.R;

public class Preferences extends PreferenceActivity {

    private static String music1 = Integer.toString(R.raw.music);
    private static String music2 = Integer.toString(R.raw.music2);
    private static String music3 = Integer.toString(R.raw.music3);

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        PreferenceScreen ps = getPreferenceManager().createPreferenceScreen(this);

        // Background music preference
        SwitchPreference music = new SwitchPreference(this);
        music.setTitle(R.string.background_music);
        music.setSummaryOn(R.string.bg_music_play);
        music.setSummaryOff(R.string.bg_music_stop);
        music.setDefaultValue(true);
        music.setKey("MUSIC_PREF");

        // Day/Night mode preference
        SwitchPreference nightMode = new SwitchPreference(this);
        nightMode.setTitle(R.string.day_night_mode);
        nightMode.setSummaryOn(R.string.day_mode_active);
        nightMode.setSummaryOff(R.string.night_mode_active);
        nightMode.setDefaultValue(true);
        nightMode.setKey("MODE_PREF");

        // Speed preference
        ListPreference speed = new ListPreference(this);
        speed.setTitle(R.string.candy_bouncing_speed);
        speed.setSummary(R.string.how_much_speed);
        speed.setKey("SPEED_PREF");
        speed.setEntries(R.array.speed_entries);
        String[] speedValues = {"5", "20", "200"};
        speed.setEntryValues(speedValues);
        speed.setDefaultValue("20");

        // Background music preference
        ListPreference soundtrack = new ListPreference(this);
        soundtrack.setTitle(R.string.soundtrack_options);
        soundtrack.setSummary(R.string.pick_soundtrack);
        soundtrack.setKey("SOUNDTRACK_PREF");
        soundtrack.setEntries(R.array.soundtrack_entries);
        String[] trackValues = {music1, music2, music3};
        soundtrack.setEntryValues(trackValues);
        soundtrack.setDefaultValue(music1);

        // Gamestyle Preferences
        ListPreference gamestyle = new ListPreference(this);
        gamestyle.setTitle(R.string.gamestyle_prefs);
        gamestyle.setSummary(R.string.pick_gamestyle);
        gamestyle.setKey("GAMESTYLE_PREF");
        gamestyle.setEntries(R.array.gamestyle_entries);
        String[] gameStyleValues = {"1", "2", "3"};
        gamestyle.setEntryValues(gameStyleValues);
        gamestyle.setDefaultValue("3");

        // Configuring preferences
        ps.addPreference(gamestyle);
        ps.addPreference(music);
        ps.addPreference(nightMode);
        ps.addPreference(speed);
        ps.addPreference(soundtrack);
        setPreferenceScreen(ps);
    }

    /**
     *
     * @param c the parent class of Activity
     * @return the value of music_pref that the user chooses, default value is true (sound on)
     */
    public static boolean soundOn(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean("MUSIC_PREF", true);
    }

    /**
     *
     * @param c the parent class of Activity
     * @return the value of mode_pref that the user chooses, default value is true (day mode)
     */
    public static boolean dayModeOn(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean("MODE_PREF", true);
    }

    /**
     *
     * @param c the parent class of Activity
     * @return an integer of the speed of the candy
     */
    public static int getSpeed(Context c) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("SPEED_PREF", "20"));
    }

    /**
     *
     * @param c the parent class of Activity
     * @return the id of the chosen soundtrack in the form of integer
     */
    public static int getSoundtrack(Context c) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("SOUNDTRACK_PREF", music1));
    }

    public static int getGameStyle(Context c) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("GAMESTYLE_PREF", "3"));
    }
}
