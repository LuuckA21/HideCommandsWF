package me.luucka.hidecommandswf.commands;

import me.luucka.hidecommandswf.HideCommandsWF;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command {

    private final HideCommandsWF plugin;

    public ReloadCommand(final HideCommandsWF plugin) {
        super("hidecommandswf", "hidecommandswf.admin");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) return;

        plugin.reload();
        sender.sendMessage(new ComponentBuilder("Plugin reloaded!").color(ChatColor.GREEN).create());
    }
}
