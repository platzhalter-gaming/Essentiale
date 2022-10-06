package net.platzhaltergaming.essentiale.paper.modules;

import org.bukkit.entity.Player;
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
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.platzhaltergaming.essentiale.paper.Main;
import net.platzhaltergaming.essentiale.paper.settings.features.JoinLeaveMessageProps;

@Getter
@RequiredArgsConstructor
public class JoinLeaveMessageModule implements Listener {

    private final Main plugin;
    private final LuckPerms luckPerms;
    private final JoinLeaveMessageProps settings;

    private final LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.legacyAmpersand();

    public void onEnable() {
        if (!getSettings().isEnabled()) {
            getPlugin().getLogger().info("Join and Leave Message Module is disabled!");
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
            event.quitMessage(this.deserializeAndTemplateMessage(getSettings().getLeaveMessage(), event));
        }
    }

    protected Component deserializeAndTemplateMessage(String message, PlayerEvent event) {
        User user = luckPerms.getPlayerAdapter(Player.class).getUser(event.getPlayer());

        String prefix = user.getCachedData().getMetaData().getPrefix();
        Component prefixComponent;
        if (prefix == null) {
            prefixComponent = Component.empty();
        } else {
            prefixComponent = legacyComponentSerializer.deserialize(prefix);
        }

        return MiniMessage.miniMessage().deserialize(message,
                Placeholder.component("display-name", event.getPlayer().displayName()),
                Placeholder.component("luckperms-prefix", prefixComponent));
    }

}
