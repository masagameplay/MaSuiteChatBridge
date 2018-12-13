package fi.matiaspaavilainen.masuitechat.commands;

import fi.matiaspaavilainen.masuitechat.MaSuiteChatBridge;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Mail implements CommandExecutor {

    private MaSuiteChatBridge plugin;

    public Mail(MaSuiteChatBridge p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {
        if (!(cs instanceof Player)) {
            return false;
        }
        Player p = (Player) cs;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        if (args.length >= 2) {
            if (args[0].equalsIgnoreCase("sendall")) {
                if (!p.hasPermission("masuitechat.mail.sendall")) {
                    p.spigot().sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.config.getMessages().getString("no-permission"))));
                    return false;
                }
            }
            if (!p.hasPermission("masuitechat.mail.send")) {
                p.spigot().sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.config.getMessages().getString("no-permission"))));
                return false;
            }
            StringBuilder msg = new StringBuilder();
            int i;
            for (i = 1; i < args.length; i++) {
                msg.append(args[i]).append(" ");
            }
            try {
                out.writeUTF("MaSuiteChat");
                out.writeUTF("Mail");
                if (!args[0].equalsIgnoreCase("sendall")) {
                    out.writeUTF("Send");
                    out.writeUTF(p.getName());
                    out.writeUTF(args[0]);
                } else {
                    out.writeUTF("SendAll");
                    out.writeUTF(p.getName());
                }
                out.writeUTF(msg.toString());
                p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("read")) {
                if (!p.hasPermission("masuitechat.mail.read")) {
                    p.spigot().sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.config.getMessages().getString("no-permission"))));
                    return false;
                }
                try {
                    out.writeUTF("MaSuiteChat");
                    out.writeUTF("Mail");
                    out.writeUTF("Read");
                    out.writeUTF(p.getName());
                    p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                p.spigot().sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.config.getSyntaxes().getString("mail.read"))));
            }

        } else {
            p.spigot().sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.config.getSyntaxes().getString("mail.all"))));
        }
        return false;
    }
}
