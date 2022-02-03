package net.platzhaltergaming.essentiale.paper.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.platzhaltergaming.commonlib.messages.Messages;
import net.platzhaltergaming.essentiale.common.settings.BaseCommandProp;
import net.platzhaltergaming.essentiale.paper.Main;
import net.platzhaltergaming.essentiale.paper.commands.base.*;
import net.platzhaltergaming.essentiale.paper.settings.SettingsHolder;

@Getter
@RequiredArgsConstructor
public class BaseCommandModule {

    private Map<String, ConstructCommandInterface> commandRegister = new ConcurrentHashMap<>();

    private final Main plugin;
    private final PaperCommandManager commandManager;
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
        // Feed Command
        this.commandRegister.put("feed", (plugin, settings, messages) -> {
            AbstractCommand command = new FeedCommand(getCommandManager(), getSettings(), getMessages());
            command.onEnable();
            return command;
        });

        // Warps Command
        this.commandRegister.put("warps", (plugin, settings, messages) -> {
            WarpsCommand command = new WarpsCommand(getCommandManager(), getSettings(), getMessages());
            command.onEnable();
            return command;
        });

        // Spawn Command
        this.commandRegister.put("spawn", (plugin, settings, messages) -> {
            if (getPlugin().getSpawnModule() == null) {
                throw new RuntimeException("spawn command requires Spawn Module to be enabled!");
            }

            SpawnCommand command = new SpawnCommand(getCommandManager(), getSettings(), getMessages(), getPlugin(),
                    getPlugin().getSpawnModule());
            command.onEnable();
            return command;
        });

        // Fly Command
        this.commandRegister.put("fly", (plugin, settings, messages) -> {
            return new FlyCommand(getCommandManager(), getSettings(), getMessages());
        });
    }

}
