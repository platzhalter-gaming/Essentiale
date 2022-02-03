package net.platzhaltergaming.essentiale.paper.settings.features;

import java.util.List;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;

@ConfigSerializable
@Data
public class WarpsProps {

    @Setting
    private long commandCooldown = 15000;

    @Setting
    private List<WarpProps> warps;

}
