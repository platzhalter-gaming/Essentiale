package net.platzhaltergaming.essentiale.paper.commands.base;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;

import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.platzhaltergaming.commonlib.messages.Messages;
import net.platzhaltergaming.essentiale.common.Cooldown;
import net.platzhaltergaming.essentiale.paper.settings.SettingsHolder;
import net.platzhaltergaming.essentiale.paper.settings.features.WarpProps;

@Getter
public class WarpsCommand extends AbstractCommand {

    private Map<String, WarpProps> warps = new HashMap<>();
    private final Cooldown<Player> cooldown;

    public WarpsCommand(PaperCommandManager commandManager, SettingsHolder settings, Messages messages) {
        super(commandManager, settings, messages);

        this.cooldown = new Cooldown<>(getSettings().getWarps().getCommandCooldown());
    }

    @Override
    public void onEnable() {
        for (WarpProps warp : getSettings().getWarps().getWarps()) {
            this.warps.put(warp.getName().toLowerCase(), warp);
        }

        getCommandManager().getCommandCompletions().registerCompletion("warps", c -> {
            return this.warps.keySet();
        });
    }

    @CommandAlias("warp")
    @CommandPermission("essentiale.server.command.warp")
    @CommandCompletion("@warps")
    public void onWarp(Player sender, String warp) {
        String warpName = warp.toLowerCase();
        if (!this.warps.containsKey(warpName)) {
            sender.sendMessage(
                    MiniMessage.miniMessage()
                            .deserialize(getMessages().get(sender.locale(), "warp-command.warp-not-found")));
            return;
        }

        // Cooldown check
        long remainingTime = cooldown.check(sender);
        if (remainingTime > 0) {
            TagResolver placeholders = TagResolver.resolver(
                    Placeholder.parsed("command", "spawn"),
                    Placeholder.parsed("remaining-seconds", String.valueOf(remainingTime)));
            sender.sendMessage(MiniMessage.miniMessage()
                    .deserialize(getMessages().get(sender.locale(), "spawn-command.cooldown-active-player"),
                            placeholders));
            return;
        }
        cooldown.add(sender);

        sender.sendMessage(MiniMessage.miniMessage()
                .deserialize(
                        getMessages().get(sender.locale(), "warp-command.teleporting").replaceAll("%warp%", warp)));
        teleportPlayerToLocation(sender, this.warps.get(warpName).getLocation());
    }

    @CommandAlias("warps")
    @CommandPermission("essentiale.server.command.warps")
    public void onWarps(Player sender) {
        String message = getMessages().get(sender.locale(), "warps-command.list.header");

        if (this.warps.size() == 0) {
            sender.sendMessage(
                    MiniMessage.miniMessage()
                            .deserialize(message + getMessages().get(sender.locale(), "warps-command.no-warps")));
            return;
        }

        String separator = getMessages().get(sender.locale(), "warps-command.list.separator");
        String warpEntry = getMessages().get(sender.locale(), "warps-command.list.entry");

        for (String warp : this.warps.keySet()) {
            message = message + warpEntry.replaceAll("%warp%", warp) + separator;
        }

        sender.sendMessage(MiniMessage.miniMessage().deserialize(message.replaceAll(separator + "$", "")));
    }

    protected void teleportPlayerToLocation(Player player, Location location) {
        // Set player velocity to 0
        player.setVelocity(new Vector(0.0, 0.0, 0.0));

        // Teleport player to location
        player.teleport(location, TeleportCause.COMMAND);
    }

}
