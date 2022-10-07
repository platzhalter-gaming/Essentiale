package net.platzhaltergaming.essentiale.paper.commands.base;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import net.platzhaltergaming.commonlib.messages.Messages;
import net.platzhaltergaming.essentiale.paper.settings.SettingsHolder;

@Getter
public abstract class AbstractCommand extends BaseCommand {

    private final PaperCommandManager commandManager;
    private final SettingsHolder settings;
    private final Messages messages;

    public AbstractCommand(PaperCommandManager commandManager, SettingsHolder settings, Messages messages) {
        this.commandManager = commandManager;
        this.settings = settings;
        this.messages = messages;

        this.setupCommandManager();
    }

    public void setupCommandManager() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

}
