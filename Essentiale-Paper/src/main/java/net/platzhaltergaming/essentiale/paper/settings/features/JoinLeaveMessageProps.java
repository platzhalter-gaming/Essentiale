package net.platzhaltergaming.essentiale.paper.settings.features;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;

@ConfigSerializable
@Data
public class JoinLeaveMessageProps {

    @Required
    @Setting
    private boolean enabled = false;

    @Setting
    private boolean hide = true;

    @Setting
    @Comment("minimessage format for player quit/leave message")
    private String joinMessage;

    @Setting
    @Comment("minimessage format for player quit/leave message")
    private String quitMessage;

}
