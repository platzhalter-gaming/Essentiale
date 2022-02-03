package net.platzhaltergaming.essentiale.bungee.commands.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.aikar.commands.BungeeCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import lombok.Getter;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.platzhaltergaming.commonlib.messages.Messages;
import net.platzhaltergaming.essentiale.bungee.modules.AfkModule;
import net.platzhaltergaming.essentiale.bungee.settings.SettingsHolder;

@CommandAlias("afk")
@CommandPermission("essentiale.bungee.command.afk")
@Description("Set yourself AFK or un-AFK yourself again")
@Getter
public class AfkCommand extends AbstractCommand {

    private final Plugin plugin;
    private final AfkModule afkModule;
    private final Pattern afkServers;

    public AfkCommand(BungeeCommandManager commandManager, SettingsHolder settings, Messages messages, Plugin plugin,
            AfkModule afkModule) {
        super(commandManager, settings, messages);

        this.plugin = plugin;
        this.afkModule = afkModule;
        this.afkServers = Pattern.compile(getSettings().getAfk().getAfkServers());
    }

    @Default
    public void onCommand(ProxiedPlayer player) {
        // Check if user is on afk server server
        ServerInfo serverInfo = player.getServer().getInfo();

        // No server? Well just send the user to the AFK servers
        ServerInfo afkServer = getPlugin().getProxy().getServerInfo(getSettings().getAfk().getAfkServer());
        if (serverInfo == null) {
            getPlugin().getLogger().fine("AFK Command: Player not on any valid server, sending to afk server: " + afkServer.getName());
            player.connect(afkServer);
            return;
        }

        getPlugin().getLogger().fine("AFK Command: Current Server: " + serverInfo.getName());
        Matcher matcher = this.afkServers.matcher(serverInfo.getName());
        if (matcher.matches()) {
            // If on one of the afk servers, return the player to the lobby server
            ServerInfo lobbyServer = getPlugin().getProxy().getServerInfo(getSettings().getAfk().getLobbyServer());
            getPlugin().getLogger().fine("AFK Command: Player on afk server, sending to lobby: " + lobbyServer.getName());
            player.connect(lobbyServer);
        } else {
            getPlugin().getLogger().fine("AFK Command: Player not on afk server, sending to afk server: " + afkServer.getName());
            player.connect(afkServer);
        }
    }

}
