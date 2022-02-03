package net.platzhaltergaming.essentiale.bungee.settings;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;

@ConfigSerializable
@Data
public class AfkProps {

    @Required
    @Setting
    private boolean enabled;

    @Setting
    private String afkServer;

    @Setting
    private String afkServers;

    @Setting
    private String lobbyServer;

}
