package net.platzhaltergaming.essentiale.bungee.modules;

import co.aikar.commands.BungeeCommandManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.platzhaltergaming.commonlib.messages.Messages;
import net.platzhaltergaming.essentiale.bungee.Main;
import net.platzhaltergaming.essentiale.bungee.settings.SettingsHolder;

@Getter
@RequiredArgsConstructor
public class AfkModule {

    private final Main plugin;
    private final BungeeCommandManager commandManager;
    private final SettingsHolder settings;
    private final Messages messages;

    public void onEnable() {
        // TODO take care of sending titles over and over again to AFK users
    }

    public void onDisable() {
    }

}
