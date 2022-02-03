package net.platzhaltergaming.essentiale.paper.modules;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.platzhaltergaming.essentiale.paper.settings.features.TabListProps;

@Getter
@RequiredArgsConstructor
public class TabListModule implements Listener {

    private final Plugin plugin;
    private final TabListProps settings;

    private Component header;
    private Component footer;

    public void onEnable() {
        if (!getSettings().isEnabled()) {
            getPlugin().getLogger().info("TabList Module is disabled!");
            return;
        }

        this.header = MiniMessage.miniMessage().deserialize(getSettings().getHeader());
        this.footer = MiniMessage.miniMessage().deserialize(getSettings().getFooter());

        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        sendPlayerListHeaderAndFooter(event.getPlayer());
    }

    protected void sendPlayerListHeaderAndFooter(Player player) {
        player.sendPlayerListHeaderAndFooter(this.header, this.footer);
    }

}
