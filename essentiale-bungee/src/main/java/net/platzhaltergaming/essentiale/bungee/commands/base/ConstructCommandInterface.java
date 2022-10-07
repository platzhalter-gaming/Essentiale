package net.platzhaltergaming.essentiale.bungee.commands.base;

import net.md_5.bungee.api.plugin.Plugin;
import net.platzhaltergaming.commonlib.messages.Messages;
import net.platzhaltergaming.essentiale.bungee.settings.SettingsHolder;

public interface ConstructCommandInterface {

    public abstract AbstractCommand create(Plugin plugin, SettingsHolder settings, Messages messages)
            throws RuntimeException;

}
