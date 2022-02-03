package net.platzhaltergaming.essentiale.paper.settings.features;

import org.bukkit.Location;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;

@ConfigSerializable
@Data
public class SpawnProps {

    @Required
    @Setting
    private boolean enabled;

    @Setting
    private long commandCooldown = 15000;

    @Setting
    private boolean onJoin = false;

    @Setting
    private boolean onRespawn = false;

    @Required
    @Setting
    private Location location;

}
