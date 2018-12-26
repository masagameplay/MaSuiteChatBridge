package fi.matiaspaavilainen.masuitechat;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.UUID;

public class ChatMessagingChannel implements PluginMessageListener {

    private MaSuiteChatBridge plugin;

    ChatMessagingChannel(MaSuiteChatBridge p) {
        plugin = p;
    }

    public void onPluginMessageReceived(String channel, Player p, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        String subchannel = null;
        try {
            subchannel = in.readUTF();
            if (subchannel.equals("LocalChat")) {
                String inMsg = in.readUTF();
                int range = in.readInt();
                Location loc = p.getLocation();
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl.getWorld().equals(p.getWorld())) {
                        if (pl.getLocation().distance(loc) <= range) {
                            TextComponent msg = new TextComponent(ComponentSerializer.parse(inMsg));
                            pl.spigot().sendMessage(msg);
                        }
                    }
                }
            }
            if (subchannel.equals("StaffChat")) {
                String inMsg = in.readUTF();
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl.hasPermission("masuitechat.channel.staff")) {
                        TextComponent msg = new TextComponent(ComponentSerializer.parse(inMsg));
                        pl.spigot().sendMessage(msg);
                    }
                }
            }

            if (subchannel.equals("GetGroup")) {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                try {
                    Player player = Bukkit.getPlayer(UUID.fromString(in.readUTF()));
                    if (p == null) {
                        return;
                    }
                    out.writeUTF("SetGroup");
                    out.writeUTF(p.getUniqueId().toString());
                    out.writeUTF(getPrefix(p));
                    out.writeUTF(getSuffix(p));
                    plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () ->
                            player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getPrefix(Player p) {
        if (plugin.getChat() != null) {
            if (plugin.getChat().getPlayerPrefix(p) != null) {
                return plugin.getChat().getPlayerPrefix(p);
            } else if (plugin.getChat().getGroupPrefix(p.getWorld(), plugin.getChat().getPrimaryGroup(p)) != null) {
                return plugin.getChat().getGroupPrefix(p.getWorld(), plugin.getChat().getPrimaryGroup(p));
            }
            return "";
        }
        return "";
    }

    private String getSuffix(Player p) {
        if (plugin.getChat() != null) {
            if (plugin.getChat().getPlayerSuffix(p) != null) {
                return plugin.getChat().getPlayerSuffix(p);
            } else if (plugin.getChat().getGroupSuffix(p.getWorld(), plugin.getChat().getPrimaryGroup(p)) != null) {
                return plugin.getChat().getGroupSuffix(p.getWorld(), plugin.getChat().getPrimaryGroup(p));
            }
            return "";
        }
        return "";
    }
}
