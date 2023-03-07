package me.luucka.hidecommandswf;

import me.luucka.hidecommandswf.commands.ReloadCommand;
import me.luucka.hidecommandswf.listeners.PlayerListenerWaterfall;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.List;
import java.util.Map;

public final class HideCommandsWF extends Plugin {

    private Settings settings;

    @Override
    public void onEnable() {
        settings = new Settings(getDataFolder());
        getProxy().getPluginManager().registerCommand(this, new ReloadCommand(this));
        getProxy().getPluginManager().registerListener(this, new PlayerListenerWaterfall(this));
    }

    public List<String> getDefaultCommands() {
        return settings.getDefaultCommands();
    }

    public Map<String, List<String>> getGroupsCommands() {
        return settings.getGroupsCommands();
    }

    public void reload() {
        this.settings.reloadConfig();
    }

}
