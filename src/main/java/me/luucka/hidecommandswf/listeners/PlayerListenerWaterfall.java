package me.luucka.hidecommandswf.listeners;

import io.github.waterfallmc.waterfall.event.ProxyDefineCommandsEvent;
import lombok.RequiredArgsConstructor;
import me.luucka.hidecommandswf.HideCommandsWF;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PlayerListenerWaterfall implements Listener {

    private final HideCommandsWF plugin;

    @EventHandler
    public void onCommandSuggestion(final ProxyDefineCommandsEvent event) {
        final ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();
        if (player.hasPermission("hidecommandswf.bypass")) return;

        final List<String> defaultCommands = new ArrayList<>(plugin.getDefaultCommands());
        final Map<String, List<String>> groupCommands = plugin.getGroupsCommands();
        for (Map.Entry<String, List<String>> entry : groupCommands.entrySet()) {
            if (player.hasPermission("hidecommandswf.group." + entry.getKey())) {
                defaultCommands.addAll(entry.getValue());
            }
        }
        event.getCommands().entrySet().removeIf(val -> !defaultCommands.contains(val.getKey()));
    }

}
