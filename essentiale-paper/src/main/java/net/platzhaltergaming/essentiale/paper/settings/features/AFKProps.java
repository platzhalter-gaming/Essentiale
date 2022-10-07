package net.platzhaltergaming.essentiale.paper.settings.features;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;

@ConfigSerializable
@Data
public class AFKProps {

    @Required
    @Setting
    private boolean enabled = true;

}
