package net.platzhaltergaming.essentiale.paper.commands.base;

import org.bukkit.entity.Player;

import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.platzhaltergaming.commonlib.messages.Messages;
import net.platzhaltergaming.essentiale.paper.settings.SettingsHolder;

@CommandAlias("fly")
@CommandPermission("essentiale.server.command.fly")
@Description("Toggle fly for yourself or a player")
@Getter
public class FlyCommand extends AbstractCommand {

    public FlyCommand(PaperCommandManager commandManager, SettingsHolder settings, Messages messages) {
        super(commandManager, settings, messages);
    }

    @Default
    public void onCommand(Player sender, @Optional Player target) {
        boolean sendOtherMessage = false;
        if (target == null || sender != target) {
            sendOtherMessage = true;
            target = sender;
        }

        boolean newFlyState = !target.isFlying();
        target.setFlying(newFlyState);

        String message = "";
        // User fly enabled/disabled message
        if (newFlyState) {
            message = getMessages().get(target.locale(), "fly-command.toggled.enabled");
        } else {
            message = getMessages().get(target.locale(), "fly-command.toggled.disabled");
        }
        target.sendMessage(
                MiniMessage.miniMessage().deserialize(message));

        if (!sendOtherMessage) {
            return;
        }

        // Command executor message
        target = (Player) sender;
        if (newFlyState) {
            message = getMessages().get(target.locale(), "fly-command.toggled.enabled.other");
        } else {
            message = getMessages().get(target.locale(), "fly-command.toggled.disabled.other");
        }
        sender.sendMessage(
                MiniMessage.miniMessage().deserialize(message,
                        Placeholder.component("player", target.displayName())));
    }

}
