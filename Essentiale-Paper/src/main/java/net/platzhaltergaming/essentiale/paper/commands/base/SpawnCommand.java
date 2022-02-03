package net.platzhaltergaming.essentiale.paper.commands.base;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.platzhaltergaming.commonlib.messages.Messages;
import net.platzhaltergaming.essentiale.common.Cooldown;
import net.platzhaltergaming.essentiale.paper.modules.SpawnModule;
import net.platzhaltergaming.essentiale.paper.settings.SettingsHolder;

@Getter(AccessLevel.PROTECTED)
@CommandAlias("spawn")
public class SpawnCommand extends AbstractCommand {

    private final JavaPlugin plugin;
    private final SpawnModule spawnModule;
    private final Cooldown<Player> cooldown;

    public SpawnCommand(PaperCommandManager commandManager, SettingsHolder settings, Messages messages,
            JavaPlugin plugin, SpawnModule spawnModule) {
        super(commandManager, settings, messages);

        this.plugin = plugin;
        this.spawnModule = spawnModule;
        this.cooldown = new Cooldown<>(getSettings().getSpawn().getCommandCooldown());
    }

    @Default
    public void onCommand(CommandSender sender, @Optional String playerName) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;

            // Cooldown check
            long remainingTime = cooldown.check(player);
            if (remainingTime > 0) {
                TagResolver placeholders = TagResolver.resolver(
                        Placeholder.parsed("command", "spawn"),
                        Placeholder.parsed("remaining-seconds", String.valueOf(remainingTime)));
                player.sendMessage(MiniMessage.miniMessage()
                        .deserialize(getMessages().get(player.locale(), "spawn-command.cooldown-active-player"),
                                placeholders));
                return;
            }
            cooldown.add(player);
        } else {
            if (playerName == null) {
                return;
            }
            player = getPlugin().getServer().getPlayer(playerName);
            if (player == null) {
                return;
            }
        }

        spawnModule.teleportToSpawn(player, TeleportCause.COMMAND);
    }

}
