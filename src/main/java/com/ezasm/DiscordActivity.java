package com.ezasm;

import java.io.File;
import java.time.Instant;
import java.util.concurrent.locks.LockSupport;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;

public final class DiscordActivity {

    private static final long discordApplicationId = 1068652802483691641L;
    private static final Activity activity;
    private static final Core core;

    static {
        Core.init(new File("discord_game_sdk.so"));

        final var params = new CreateParams();
        params.setClientID(discordApplicationId);

        core = new Core(params);

        activity = new Activity();
        activity.setDetails("I'm using EzASM");
        activity.setState("EzASM is easy");
        activity.timestamps().setStart(Instant.now());

        updateActivity();
    }

    /**
     * This is an infinite loop... use a separate thread to
     * run the core.
     */
    public static void runCore() {
        while (true) {
            core.runCallbacks();
            LockSupport.parkNanos(16_000_000);
        }
    }

    /**
     * The "details" field is the first line of text under the title when
     * looking at the activity on Discord.
     * 
     * @param details
     */
    public static void setDetails(String details) {
        activity.setDetails(details);
        updateActivity();
    }

    /**
     * The "state" field is the second line of text under the title when
     * looking at the activity on Discord.
     * 
     * @param state
     */
    public static void setState(String state) {
        activity.setState(state);
        updateActivity();
    }

    private static void updateActivity() {
        core.activityManager().updateActivity(activity);
    }

}