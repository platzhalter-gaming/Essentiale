package net.platzhaltergaming.essentiale.paper.modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.platzhaltergaming.essentiale.paper.settings.features.AFKProps;

@Getter
@RequiredArgsConstructor
public class AFKModule implements Listener {

    private final JavaPlugin plugin;
    private final AFKProps settings;

    public void onEnable() {
        if (!getSettings().isEnabled()) {
            getPlugin().getLogger().info("AFK Module is disabled!");
            return;
        }

        // TODO
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        // TODO
    }

}
