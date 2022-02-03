package net.platzhaltergaming.essentiale.paper.settings.features;

import java.util.List;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;

@ConfigSerializable
@Data
public class ScoreboardProps {

    @Required
    @Setting
    private boolean enabled = false;

    @Setting
    private int refreshSeconds = 5;

    @Setting
    private String title;

    @Setting
    private List<String> lines;

}
