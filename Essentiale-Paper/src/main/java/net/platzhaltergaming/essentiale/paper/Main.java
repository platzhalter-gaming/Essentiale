package net.platzhaltergaming.essentiale.paper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Locale;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.platzhaltergaming.commonlib.messages.Messages;
import net.platzhaltergaming.essentiale.paper.configurate.LocaleSerializer;
import net.platzhaltergaming.essentiale.paper.configurate.LocationSerializer;
import net.platzhaltergaming.essentiale.paper.modules.BaseCommandModule;
import net.platzhaltergaming.essentiale.paper.modules.JoinLeaveMessageModule;
import net.platzhaltergaming.essentiale.paper.modules.NameTagModule;
import net.platzhaltergaming.essentiale.paper.modules.ScoreboardModule;
import net.platzhaltergaming.essentiale.paper.modules.SpawnModule;
import net.platzhaltergaming.essentiale.paper.modules.TabListModule;
import net.platzhaltergaming.essentiale.paper.modules.WorldLoaderModule;
import net.platzhaltergaming.essentiale.paper.settings.SettingsHolder;

@Getter
public class Main extends JavaPlugin {

    // Config
    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private SettingsHolder settings;
    private Messages messages;

    // Base
    private PaperCommandManager commandManager;
    private LuckPerms luckPerms;

    // Modules
    private WorldLoaderModule worldLoaderModule;
    private NameTagModule nameTagModule;
    private TabListModule tabListModule;
    private ScoreboardModule scoreboardModule;
    private SpawnModule spawnModule;
    private BaseCommandModule baseCommandModule;
    private JoinLeaveMessageModule joinLeaveMessageModule;

    @Override
    public void onEnable() {
        // Config
        this.loadConfig();
        this.messages = new Messages(getDataFolder().toPath(), "messages", getSettings().getLocales(),
                getSettings().getLocales().get(0));

        // Base
        this.commandManager = new PaperCommandManager(this);
        this.commandManager.enableUnstableAPI("help");
        this.commandManager.enableUnstableAPI("brigadier");
        this.luckPerms = LuckPermsProvider.get();
        PlaceholderResolver.placeholderAPIEnabled = (getServer().getPluginManager()
                .getPlugin("PlaceholderAPI") != null);

        // Modules
        this.worldLoaderModule = new WorldLoaderModule(this, getSettings().getWorldLoader());
        this.worldLoaderModule.onEnable();

        this.nameTagModule = new NameTagModule(this, getLuckPerms(), getSettings().getNameTag());
        this.nameTagModule.onEnable();

        this.tabListModule = new TabListModule(this, getSettings().getTabList());
        this.tabListModule.onEnable();

        this.scoreboardModule = new ScoreboardModule(this, getSettings().getScoreboard());
        this.scoreboardModule.onEnable();

        this.spawnModule = new SpawnModule(this, getSettings().getSpawn());
        this.spawnModule.onEnable();

        this.baseCommandModule = new BaseCommandModule(this, this.commandManager, getSettings(), getMessages());
        this.baseCommandModule.onEnable();

        this.joinLeaveMessageModule = new JoinLeaveMessageModule(this, getLuckPerms(), getSettings().getJoinLeaveMessage());
        this.joinLeaveMessageModule.onEnable();
    }

    @Override
    public void onDisable() {
        if (this.nameTagModule != null) {
            this.nameTagModule.onDisable();
        }
        if (this.scoreboardModule != null) {
            this.scoreboardModule.onDisable();
        }
    }

    public void loadConfig() {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getClassLoader().getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LocationSerializer.INSTANCE = new LocationSerializer(this);
        loader = YamlConfigurationLoader.builder()
                .defaultOptions(
                        opts -> opts.serializers(build -> {
                            build.register(Location.class, LocationSerializer.INSTANCE);
                            build.register(Locale.class, LocaleSerializer.INSTANCE);
                        }))
                .file(file).build();

        CommentedConfigurationNode root;
        try {
            root = loader.load();
        } catch (IOException e) {
            getLogger().severe("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            settings = root.get(SettingsHolder.class);
        } catch (SerializationException e) {
            getLogger().severe("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

}
