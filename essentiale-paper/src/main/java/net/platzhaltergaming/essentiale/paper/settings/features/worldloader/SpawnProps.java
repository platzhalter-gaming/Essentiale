package net.platzhaltergaming.essentiale.paper.settings.features.worldloader;

import org.bukkit.Location;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;

@ConfigSerializable
@Data
public class SpawnProps {

    @Setting
    private boolean enabled = false;

    @Setting
    private Location location;

}
