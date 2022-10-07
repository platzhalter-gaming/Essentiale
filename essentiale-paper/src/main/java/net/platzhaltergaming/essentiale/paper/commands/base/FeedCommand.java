package net.platzhaltergaming.essentiale.paper.commands.base;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.CommandHelp;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.platzhaltergaming.commonlib.messages.Messages;
import net.platzhaltergaming.essentiale.paper.settings.SettingsHolder;

@CommandAlias("feed")
@CommandPermission("essentiale.server.command.feed")
@Description("Feed yourself or another player")
public class FeedCommand extends AbstractCommand {

    public FeedCommand(PaperCommandManager commandManager, SettingsHolder settings, Messages messages) {
        super(commandManager, settings, messages);
    }

    @Subcommand("add")
    @CommandPermission("essentiale.server.command.feed.add")
    @CommandCompletion("@range:0-20 @players")
    public void onAddCommand(CommandSender sender, @Optional Integer hunger, @Optional OnlinePlayer player) {
        Player target;
        if (player == null) {
            if (!(sender instanceof Player)) {
                throw new InvalidCommandArgument(getMessages().get(null, "command.console"), false);
            }
            target = (Player) sender;
        } else {
            target = player.getPlayer();
            if (!sender.hasPermission("essentiale.server.command.feed.others")) {
                throw new InvalidCommandArgument(getMessages().get(null, "feed-command.others.no-permission"), false);
            }
        }

        setHunger(target, target.getFoodLevel() + hunger);
    }

    @Subcommand("set")
    @CommandPermission("essentiale.server.command.feed.set")
    @CommandCompletion("@range:0-20 @players")
    public void onSetCommand(CommandSender sender, @Optional Integer hunger, @Optional OnlinePlayer player) {
        Player target;
        if (player == null) {
            if (!(sender instanceof Player)) {
                throw new InvalidCommandArgument(getMessages().get(null, "command.console"), false);
            }
            target = (Player) sender;
        } else {
            target = player.getPlayer();
            if (!sender.hasPermission("essentiale.server.command.feed.others")) {
                throw new InvalidCommandArgument(getMessages().get(null, "feed-command.others.no-permission"), false);
            }
        }

        setHunger(target, hunger);
    }

    @Subcommand("remove")
    @CommandPermission("essentiale.server.command.feed.remove")
    @CommandCompletion("@range:0-20 @players")
    public void onRemoveCommand(CommandSender sender, @Optional Integer hunger, @Optional OnlinePlayer player) {
        Player target;
        if (player == null) {
            if (!(sender instanceof Player)) {
                throw new InvalidCommandArgument(getMessages().get(null, "command.console"), false);
            }
            target = (Player) sender;
        } else {
            target = player.getPlayer();
            if (!sender.hasPermission("essentiale.server.command.feed.others")) {
                throw new InvalidCommandArgument(getMessages().get(null, "feed-command.others.no-permission"), false);
            }
        }

        setHunger(target, target.getFoodLevel() - hunger);
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    protected void setHunger(Player player, Integer hunger) {
        player.setFoodLevel(hunger == null ? 20 : hunger);
    }

}
