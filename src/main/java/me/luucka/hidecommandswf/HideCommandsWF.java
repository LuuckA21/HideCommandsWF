package me.luucka.hidecommandswf;

import lombok.Getter;
import me.luucka.hidecommandswf.config.Settings;
import me.luucka.hidecommandswf.listeners.PlayerListenerWaterfall;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.List;
import java.util.Map;

public final class HideCommandsWF extends Plugin {

    @Getter
    private Settings settings;

    @Override
    public void onEnable() {
        this.settings = new Settings(getDataFolder());
        ;
        getProxy().getPluginManager().registerListener(this, new PlayerListenerWaterfall(this));
    }

    public List<String> getDefaultCommands() {
        return settings.getDefaultCommands();
    }

    public Map<String, List<String>> getGroupsCommands() {
        return settings.getGroupsCommands();
    }
}
