package me.luucka.hidecommandswf.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.ParsingException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BaseConfiguration {

    private static final Logger LOGGER = Logger.getLogger("HideCommandsWF");

    private final Class<?> resourceClass = BaseConfiguration.class;
    private final File configFile;
    private final YamlConfigurationLoader loader;
    private final String templateName;
    private CommentedConfigurationNode configurationNode;

    public BaseConfiguration(final File configFile, final String templateName) {
        this.configFile = configFile;
        this.loader = YamlConfigurationLoader.builder()
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .file(configFile)
                .build();
        this.templateName = templateName;
    }

    public Set<String> getKeys(final CommentedConfigurationNode configurationNode) {
        if (configurationNode == null || !configurationNode.isMap()) {
            return Collections.emptySet();
        }

        final Set<String> keys = new LinkedHashSet<>();
        for (Object obj : configurationNode.childrenMap().keySet()) {
            keys.add(String.valueOf(obj));
        }
        return keys;
    }

    public <T> List<T> getList(final String path, Class<T> type) {
        final CommentedConfigurationNode node = getInternal(path);
        if (node == null) {
            return new ArrayList<>();
        }

        try {
            final List<T> list = node.getList(type);
            if (list == null) return new ArrayList<>();
            return list;
        } catch (SerializationException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public CommentedConfigurationNode getSection(final String path) {
        final CommentedConfigurationNode node = toSplitRoot(path, configurationNode);
        if (node.virtual()) return null;
        return node;
    }

    private CommentedConfigurationNode getInternal(final String path) {
        final CommentedConfigurationNode node = toSplitRoot(path, configurationNode);
        if (node.virtual()) return null;
        return node;
    }

    public CommentedConfigurationNode toSplitRoot(String path, final CommentedConfigurationNode node) {
        if (path == null) return node;

        path = path.startsWith(".") ? path.substring(1) : path;
        return node.node(path.contains(".") ? path.split("\\.") : new Object[]{path});
    }

    public void load() {
        if (configFile.getParentFile() != null && !configFile.getParentFile().exists()) {
            if (!configFile.getParentFile().mkdirs()) {
                LOGGER.log(Level.SEVERE, "Failed to create config: ", configFile.toString());
            }
        }

        if (!configFile.exists()) {
            try {
                if (templateName != null) {
                    Files.copy(resourceClass.getResourceAsStream(templateName), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    this.configFile.createNewFile();
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to create config " + configFile, e);
            }
        }

        try {
            configurationNode = loader.load();
        } catch (final ParsingException e) {
            final File broken = new File(configFile.getAbsolutePath() + ".broken." + System.currentTimeMillis());
            if (configFile.renameTo(broken)) {
                LOGGER.log(Level.SEVERE, "The file " + configFile + " is broken, it has been renamed to " + broken, e.getCause());
                return;
            }
            LOGGER.log(Level.SEVERE, "The file " + configFile + " is broken. A backup file has failed to be created", e.getCause());
        } catch (final ConfigurateException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);;
        } finally {
            if (configurationNode == null) {
                configurationNode = loader.createNode();
            }
        }
    }
}