package net.platzhaltergaming.essentiale.common.settings;

import java.util.Map;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;

@ConfigSerializable
@Data
public class BaseCommandProps {

    @Setting
    private Map<String, BaseCommandProp> commands;

}
