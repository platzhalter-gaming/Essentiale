package net.platzhaltergaming.essentiale.paper.modules;

import java.util.Map.Entry;

import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.platzhaltergaming.essentiale.paper.settings.features.WorldLoaderProps;
import net.platzhaltergaming.essentiale.paper.settings.features.worldloader.GameRuleValue;
import net.platzhaltergaming.essentiale.paper.settings.features.worldloader.WorldProps;

@Getter
@RequiredArgsConstructor
public class WorldLoaderModule {

    private final JavaPlugin plugin;
    private final WorldLoaderProps settings;

    public void onEnable() {
        if (!getSettings().isEnabled()) {
            getPlugin().getLogger().info("World Loader Module is disabled!");
            return;
        }

        for (Entry<String, WorldProps> entry : getSettings().getWorlds().entrySet()) {
            String worldName = entry.getKey();
            WorldProps props = entry.getValue();

            if (props.isEnabled()) {
                getPlugin().getLogger().info(String.format("Loading/Setting options for world %s ...", worldName));

                World world = getPlugin().getServer().getWorld(worldName);
                // If the world does not exist, create it
                if (world == null) {
                    WorldCreator wc = new WorldCreator(worldName);
                    wc.generateStructures(props.isGenerateStructures());
                    if (props.getSeed() != -1) {
                        wc.seed(props.getSeed());
                    }
                    world = getPlugin().getServer().createWorld(wc);
                }

                // Set settings for the world
                if (props.getDifficulty() != null) {
                    world.setDifficulty(props.getDifficulty());
                }
                world.setPVP(props.isPvp());

                if (props.getSpawn().isEnabled() && props.getSpawn().getLocation() != null) {
                    world.setSpawnLocation(props.getSpawn().getLocation());
                }

                if (props.getWorldBorder().isEnabled()) {
                    if (props.getWorldBorder().getCenter() != null) {
                        world.getWorldBorder().setCenter(props.getWorldBorder().getCenter());
                    }
                    if (props.getWorldBorder().getSize() > 0) {
                        world.getWorldBorder().setSize(props.getWorldBorder().getSize());
                    }
                }

                setGameRules(props, world);

                getPlugin().getLogger().info(String.format("Loaded world %s.", worldName));
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void setGameRules(WorldProps props, World world) {
        // Set global game rules
        for (Entry<String, GameRuleValue> entry : getSettings().getDefaultGameRules().entrySet()) {
            GameRule<?> rule = GameRule.getByName(entry.getKey());
            if (rule == null) {
                getPlugin().getLogger().warning(String.format("Default game rule named %s not found!", entry.getKey()));
                continue;
            }

            if (entry.getValue().getBool() != null) {
                world.setGameRule((GameRule<Boolean>) rule, entry.getValue().getBool());
            } else {
                world.setGameRule((GameRule<Integer>) rule, entry.getValue().getInteger());
            }
        }

        // Set per world game rules
        for (Entry<String, GameRuleValue> entry : props.getGameRules().entrySet()) {
            GameRule<?> rule = GameRule.getByName(entry.getKey());
            if (rule == null) {
                getPlugin().getLogger().warning(String.format("Game rule named %s not found!", entry.getKey()));
                continue;
            }

            if (entry.getValue().getBool() != null) {
                world.setGameRule((GameRule<Boolean>) rule, entry.getValue().getBool());
            } else {
                world.setGameRule((GameRule<Integer>) rule, entry.getValue().getInteger());
            }
        }
    }

}
