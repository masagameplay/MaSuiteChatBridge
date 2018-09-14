package fi.matiaspaavilainen.masuitechat;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Channel implements PluginMessageListener {

    private static MaSuiteChatBridge plugin;

    Channel(MaSuiteChatBridge p) {
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
                LocalChat.sendMessage(p, in.readUTF(), in.readInt());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
