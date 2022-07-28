package net.platzhaltergaming.essentiale.paper.modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.platzhaltergaming.essentiale.paper.Main;
import net.platzhaltergaming.essentiale.paper.settings.features.JoinLeaveMessageProps;

@Getter
@RequiredArgsConstructor
public class JoinLeaveMessageModule implements Listener {

    private final Main plugin;
    private final JoinLeaveMessageProps settings;

    private Component joinMessage;
    private Component quitMessage;

    public void onEnable() {
        if (!getSettings().isEnabled()) {
            getPlugin().getLogger().info("Spawn Module is disabled!");
            return;
        }

        this.joinMessage = MiniMessage.miniMessage().deserialize(getSettings().getJoinMessage());
        this.quitMessage = MiniMessage.miniMessage().deserialize(getSettings().getQuitMessage());

        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (getSettings().isHide()) {
            event.joinMessage(Component.empty());
        } else {
            event.joinMessage(this.joinMessage);
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        if (getSettings().isHide()) {
            event.quitMessage(Component.empty());
        } else {
            event.quitMessage(this.quitMessage);
        }
    }

}
