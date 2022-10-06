package net.platzhaltergaming.essentiale.paper.modules;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.platzhaltergaming.essentiale.paper.Main;
import net.platzhaltergaming.essentiale.paper.settings.features.JoinLeaveMessageProps;

@Getter
@RequiredArgsConstructor
public class JoinLeaveMessageModule implements Listener {

    private final Main plugin;
    private final JoinLeaveMessageProps settings;

    public void onEnable() {
        if (!getSettings().isEnabled()) {
            getPlugin().getLogger().info("Spawn Module is disabled!");
            return;
        }

        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (getSettings().isHide()) {
            event.joinMessage(Component.empty());
        } else {
            event.joinMessage(this.deserializeAndTemplateMessage(getSettings().getJoinMessage(), event));
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        if (getSettings().isHide()) {
            event.quitMessage(Component.empty());
        } else {
            event.quitMessage(this.deserializeAndTemplateMessage(getSettings().getQuitMessage(), event));
        }
    }

    protected Component deserializeAndTemplateMessage(String message, PlayerEvent event) {
        return MiniMessage.miniMessage().deserialize(getSettings().getJoinMessage(),
                Placeholder.component("display-name", event.getPlayer().displayName()));
    }

}
