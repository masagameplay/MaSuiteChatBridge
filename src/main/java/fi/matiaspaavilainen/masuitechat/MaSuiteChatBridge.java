package fi.matiaspaavilainen.masuitechat;

import fi.matiaspaavilainen.masuitechat.commands.Mail;
import fi.matiaspaavilainen.masuitechat.commands.Nick;
import fi.matiaspaavilainen.masuitechat.commands.Reply;
import fi.matiaspaavilainen.masuitechat.commands.channels.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MaSuiteChatBridge extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new Channel(this));



        registerCommands();
    }


    private void registerCommands(){
        // Channels
        getCommand("staff").setExecutor(new Staff(this));
        getCommand("global").setExecutor(new Global(this));
        getCommand("server").setExecutor(new Server(this));
        getCommand("local").setExecutor(new Local(this));
        getCommand("tell").setExecutor(new Private(this));
        getCommand("reply").setExecutor(new Reply(this));

        // Nick
        getCommand("nick").setExecutor(new Nick(this));

        // Mail
        getCommand("mail").setExecutor(new Mail(this));
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMessage(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("MaSuiteChat");
            out.writeUTF("Chat");
            out.writeUTF(p.getUniqueId().toString());
            out.writeUTF(e.getMessage());
            p.sendPluginMessage(this, "BungeeCord", b.toByteArray());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
