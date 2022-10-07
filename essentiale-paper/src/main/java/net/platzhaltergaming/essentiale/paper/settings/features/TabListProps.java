package net.platzhaltergaming.essentiale.paper.settings.features;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;

@ConfigSerializable
@Data
public class TabListProps {

    @Required
    @Setting
    private boolean enabled;

    @Setting
    private String header;

    @Setting
    private String footer;

}
