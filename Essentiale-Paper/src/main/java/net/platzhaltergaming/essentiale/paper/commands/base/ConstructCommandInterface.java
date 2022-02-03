package net.platzhaltergaming.essentiale.paper.commands.base;

import org.bukkit.plugin.java.JavaPlugin;

import net.platzhaltergaming.commonlib.messages.Messages;
import net.platzhaltergaming.essentiale.paper.settings.SettingsHolder;

public interface ConstructCommandInterface {

    public abstract AbstractCommand create(JavaPlugin plugin, SettingsHolder settings, Messages messages)
            throws RuntimeException;

}
