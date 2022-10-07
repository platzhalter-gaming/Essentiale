package net.platzhaltergaming.essentiale.paper;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

public final class PlaceholderResolver {

    public static boolean placeholderAPIEnabled = false;

    public static String apply(Player player, String line) {
        if (placeholderAPIEnabled) {
            return PlaceholderAPI.setPlaceholders(player, line);
        } else {
            return line;
        }
    }

    private PlaceholderResolver() {
    }

}
