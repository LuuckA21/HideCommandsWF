package me.luucka.hidecommandswf.config;

import lombok.Getter;

import java.io.File;
import java.util.*;

public class Settings {

    private final BaseConfiguration config;

    @Getter
    private final List<String> defaultCommands = new ArrayList<>();

    @Getter
    private final Map<String, List<String>> groupsCommands = new HashMap<>();

    public Settings(final File dataFolder) {
        this.config = new BaseConfiguration(new File(dataFolder, "config.yml"), "/config.yml");
        reloadConfig();
    }

    public void reloadConfig() {
        config.load();

        defaultCommands.clear();
        defaultCommands.addAll(config.getList("default", String.class));

        groupsCommands.clear();
        final Set<String> keys = config.getKeys(config.getSection("groups"));
        for (final String k : keys) {
            groupsCommands.put(k, config.getList("groups." + k + ".commands", String.class));
        }
    }
}
