package me.luucka.hidecommandswf.listeners;

import io.github.waterfallmc.waterfall.event.ProxyDefineCommandsEvent;
import lombok.RequiredArgsConstructor;
import me.luucka.hidecommandswf.HideCommandsWF;
import me.luucka.hidecommandswf.Perms;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PlayerListenerWaterfall implements Listener {

    private final HideCommandsWF PLUGIN;

    @EventHandler
    public void onCommandSuggestion(ProxyDefineCommandsEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();
        if (player.hasPermission(Perms.ADMIN)) return;

        List<String> commands = new ArrayList<>(PLUGIN.getDefaultCommands());

        for (Map.Entry<String, List<String>> entry : PLUGIN.getGroupsCommands().entrySet()) {
            if (player.hasPermission(Perms.GROUP + entry.getKey())) {
                commands.addAll(entry.getValue());
            }
        }

        event.getCommands().entrySet().removeIf(val -> !commands.contains(val.getKey()));
    }

}
