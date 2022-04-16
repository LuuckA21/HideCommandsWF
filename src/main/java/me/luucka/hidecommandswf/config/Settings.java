package me.luucka.hidecommandswf.config;

import lombok.Getter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Settings {

    private final BaseConfiguration config;

    @Getter
    private List<String> defaultCommands;

    @Getter
    private Map<String, List<String>> groupsCommands;

    public Settings(final File dataFolder) {
        this.config = new BaseConfiguration(new File(dataFolder, "config.yml"), "/config.yml");
        reloadConfig();
    }

    public void reloadConfig() {
        config.load();
        defaultCommands = _getDefaultCommands();
        groupsCommands = _getGroupsCommands();
    }

    private List<String> _getDefaultCommands() {
        return config.getList("default", String.class);
    }

    private Map<String, List<String>> _getGroupsCommands() {
        Map<String, List<String>> groupsCommands = new HashMap<>();
        final Set<String> keys = config.getKeys(config.getSection("groups"));
        for (final String k : keys) {
            List<String> commands = config.getList("groups." + k + ".commands", String.class);
            groupsCommands.put(k, commands);
        }
        return groupsCommands;
    }
}
