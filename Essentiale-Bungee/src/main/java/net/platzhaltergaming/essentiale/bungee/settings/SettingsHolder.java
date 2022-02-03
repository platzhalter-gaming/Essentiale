package net.platzhaltergaming.essentiale.bungee.settings;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import lombok.Data;
import net.platzhaltergaming.essentiale.common.settings.BaseCommandProps;

@ConfigSerializable
@Data
public class SettingsHolder {

    @Setting
    private List<Locale> locales = Arrays.asList(Locale.ENGLISH, Locale.GERMAN);

    @Required
    @Setting
    private BaseCommandProps baseCommands;

    @Setting
    private AfkProps afk;

}
