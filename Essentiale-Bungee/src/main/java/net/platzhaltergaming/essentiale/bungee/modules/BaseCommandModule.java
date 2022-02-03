package net.platzhaltergaming.essentiale.bungee.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import co.aikar.commands.BungeeCommandManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.platzhaltergaming.commonlib.messages.Messages;
import net.platzhaltergaming.essentiale.bungee.Main;
import net.platzhaltergaming.essentiale.bungee.commands.base.*;
import net.platzhaltergaming.essentiale.common.settings.BaseCommandProp;
import net.platzhaltergaming.essentiale.bungee.settings.SettingsHolder;

@Getter
@RequiredArgsConstructor
public class BaseCommandModule {

    private Map<String, ConstructCommandInterface> commandRegister = new ConcurrentHashMap<>();

    private final Main plugin;
    private final BungeeCommandManager commandManager;
    private final SettingsHolder settings;
    private final Messages messages;

    private List<AbstractCommand> commands = new ArrayList<>();

    public void onEnable() {
        this.registerCommandConstructors();

        for (Entry<String, BaseCommandProp> entry : settings.getBaseCommands().getCommands().entrySet()) {
            if (!entry.getValue().isEnabled()) {
                continue;
            }

            if (commandRegister.containsKey(entry.getKey())) {
                try {
                    commands.add(commandRegister.get(entry.getKey()).create(getPlugin(), getSettings(), getMessages()));
                    getPlugin().getLogger().info(String.format("Registering %s module commands!", entry.getKey()));
                } catch (RuntimeException exc) {
                    getPlugin().getLogger().warning(String.format("Exception while registering %s module commands!: %s",
                            entry.getKey(), exc.getMessage()));
                }
            } else {
                getPlugin().getLogger().warning("Unknown command in base-commands.commands list!");
            }
        }

        for (AbstractCommand command : commands) {
            commandManager.registerCommand(command);
        }
    }

    // All commands need to have their constructor added to this method
    protected void registerCommandConstructors() {
        // AFK Command
        this.commandRegister.put("afk", (plugin, settings, messages) -> {
            if (getPlugin().getAfkModule() == null) {
                throw new RuntimeException("afk command requires AFK Module to be enabled!");
            }

            AfkCommand command = new AfkCommand(getCommandManager(), getSettings(), getMessages(), getPlugin(),
                    getPlugin().getAfkModule());
            command.onEnable();
            return command;
        });
    }

}
