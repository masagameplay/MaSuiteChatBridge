package fi.matiaspaavilainen.masuitechat.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitechat.MaSuiteChatBridge;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if (args.length > 2) {


            StringBuilder msg = new StringBuilder();
            int i;
            for (i = 1; i < args.length; i++) {
                msg.append(args[i]).append(" ");
            }

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("MaSuiteChat");
            out.writeUTF("Mail");
            out.writeUTF("Send");
            out.writeUTF(p.getName());
            out.writeUTF(args[0]);
            out.writeUTF(msg.toString());
            p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        } else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("read")){
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("MaSuiteChat");
                out.writeUTF("Mail");
                out.writeUTF("Read");
                out.writeUTF(p.getName());
                p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            }

        }
        return false;
    }
}
