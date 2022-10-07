package net.platzhaltergaming.essentiale.paper.settings.features;

import java.util.Map;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;
import net.platzhaltergaming.essentiale.paper.settings.features.worldloader.GameRuleValue;
import net.platzhaltergaming.essentiale.paper.settings.features.worldloader.WorldProps;

@ConfigSerializable
@Data
public class WorldLoaderProps {

    @Required
    @Setting
    private boolean enabled;

    @Setting
    private Map<String, GameRuleValue> defaultGameRules;

    @Setting
    private Map<String, WorldProps> worlds;

}
