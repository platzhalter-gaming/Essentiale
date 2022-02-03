package net.platzhaltergaming.essentiale.paper.settings.features.worldloader;

import java.util.Map;

import org.bukkit.Difficulty;
import org.bukkit.World.Environment;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;

@ConfigSerializable
@Data
public class WorldProps {

    @Required
    @Setting
    private boolean enabled = false;

    @Setting
    private Environment environment = Environment.NORMAL;

    @Setting
    private boolean generateStructures = true;

    @Setting
    private long seed = -1;

    @Setting
    private Difficulty difficulty = Difficulty.PEACEFUL;

    @Setting
    private boolean pvp = false;

    @Setting
    private SpawnProps spawn;

    @Setting
    private Map<String, GameRuleValue> gameRules;

    @Setting
    private WorldBorderProps worldBorder;

}