package net.platzhaltergaming.essentiale.paper.settings.features;

import org.bukkit.Location;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;

@ConfigSerializable
@Data
public class WarpProps {

    @Required
    @Setting
    private String name;

    @Setting
    private String permission;

    @Required
    @Setting
    private Location location;

}
