package fi.matiaspaavilainen.masuitechat;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitechat.commands.Mail;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MaSuiteChatBridge extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new Channel(this));

        getCommand("mail").setExecutor(new Mail(this));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMessage(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("MaSuiteChat");
        out.writeUTF("Chat");
        out.writeUTF(p.getName());
        out.writeUTF(e.getMessage());
        p.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }
}
