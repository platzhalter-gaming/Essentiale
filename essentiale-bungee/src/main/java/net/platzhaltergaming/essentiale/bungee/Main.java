package net.platzhaltergaming.essentiale.bungee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Locale;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import co.aikar.commands.BungeeCommandManager;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.platzhaltergaming.commonlib.messages.Messages;
import net.platzhaltergaming.essentiale.bungee.modules.AfkModule;
import net.platzhaltergaming.essentiale.bungee.modules.BaseCommandModule;
import net.platzhaltergaming.essentiale.bungee.settings.SettingsHolder;
import net.platzhaltergaming.essentiale.bungee.configurate.LocaleSerializer;

@Getter
public class Main extends Plugin {

    // Config
    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private SettingsHolder settings;
    private Messages messages;

    // Base
    private BungeeCommandManager commandManager;

    // Modules
    private AfkModule afkModule;
    private BaseCommandModule baseCommandModule;

    @Override
    public void onEnable() {
        // Config
        this.loadConfig();
        this.messages = new Messages(getDataFolder().toPath(), "messages", getSettings().getLocales(),
                getSettings().getLocales().get(0));

        // Base
        this.commandManager = new BungeeCommandManager(this);

        // Modules
        this.afkModule = new AfkModule(this, getCommandManager(), getSettings(), getMessages());
        this.afkModule.onEnable();

        this.baseCommandModule = new BaseCommandModule(this, getCommandManager(), getSettings(), getMessages());
        this.baseCommandModule.onEnable();
    }

    @Override
    public void onDisable() {
        if (this.afkModule != null) {
            this.afkModule.onDisable();
        }
    }

    public void loadConfig() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loader = YamlConfigurationLoader.builder()
                .defaultOptions(
                        opts -> opts.serializers(build -> build.register(Locale.class, LocaleSerializer.INSTANCE)))
                .file(file).build();

        CommentedConfigurationNode root;
        try {
            root = loader.load();
        } catch (IOException e) {
            getLogger().severe("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            return;
        }

        try {
            settings = root.get(SettingsHolder.class);
        } catch (SerializationException e) {
            getLogger().severe("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            return;
        }
    }

}
