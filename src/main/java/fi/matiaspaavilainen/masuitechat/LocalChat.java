package fi.matiaspaavilainen.masuitechat;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocalChat {

    public static void sendMessage(Player p, String message, Integer range) {
        Location loc = p.getLocation();
        for(Player pl : Bukkit.getOnlinePlayers()){
            if(pl.getLocation().distance(loc) <= range){
                TextComponent msg = new TextComponent(ComponentSerializer.parse(message));
                pl.spigot().sendMessage(msg);
            }
        }
    }
}
