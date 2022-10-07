package net.platzhaltergaming.essentiale.common.settings;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;

@ConfigSerializable
@Data
public class BaseCommandProp {

    @Required
    @Setting
    private boolean enabled = true;

}
