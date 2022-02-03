package net.platzhaltergaming.essentiale.paper.settings.features;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;

@ConfigSerializable
@Data
public class NameTagProps {

    @Required
    @Setting
    private boolean enabled = true;

    @Setting
    private int refreshSeconds = 7;

    @Setting
    private int maxWeight = 10000;

    @Setting
    private String prefix;

}
