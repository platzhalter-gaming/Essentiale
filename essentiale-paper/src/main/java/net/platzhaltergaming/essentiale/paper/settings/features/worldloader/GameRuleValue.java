package net.platzhaltergaming.essentiale.paper.settings.features.worldloader;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;

@ConfigSerializable
@Data
public class GameRuleValue {

    @Setting
    private Boolean bool;

    @Setting
    private Integer integer;

}
