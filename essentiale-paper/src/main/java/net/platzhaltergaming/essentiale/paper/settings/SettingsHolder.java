package net.platzhaltergaming.essentiale.paper.settings;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;
import net.platzhaltergaming.essentiale.common.settings.BaseCommandProps;
import net.platzhaltergaming.essentiale.paper.settings.features.JoinLeaveMessageProps;
import net.platzhaltergaming.essentiale.paper.settings.features.NameTagProps;
import net.platzhaltergaming.essentiale.paper.settings.features.ScoreboardProps;
import net.platzhaltergaming.essentiale.paper.settings.features.SpawnProps;
import net.platzhaltergaming.essentiale.paper.settings.features.TabListProps;
import net.platzhaltergaming.essentiale.paper.settings.features.WarpsProps;
import net.platzhaltergaming.essentiale.paper.settings.features.WorldLoaderProps;

@ConfigSerializable
@Data
public class SettingsHolder {

    @Setting
    private List<Locale> locales = Arrays.asList(Locale.ENGLISH, Locale.GERMAN);

    @Required
    @Setting
    private BaseCommandProps baseCommands;

    @Required
    @Setting
    private TabListProps tabList;

    @Required
    @Setting
    private NameTagProps nameTag;

    @Required
    @Setting
    private ScoreboardProps scoreboard;

    @Setting
    private WarpsProps warps;

    @Setting
    private SpawnProps spawn;

    @Required
    @Setting
    private WorldLoaderProps worldLoader;

    @Setting
    private JoinLeaveMessageProps joinLeaveMessage;

}
