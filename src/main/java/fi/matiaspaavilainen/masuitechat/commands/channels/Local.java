package fi.matiaspaavilainen.masuitechat.commands.channels;

import com.google.common.base.Joiner;
import fi.matiaspaavilainen.masuitechat.MaSuiteChatBridge;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Local implements CommandExecutor {

    private MaSuiteChatBridge plugin;

    public Local(MaSuiteChatBridge p) {
        plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player p = (Player) sender;
        if (args.length == 0) {
            try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(b)) {
                out.writeUTF("MaSuiteChat");
                out.writeUTF("ToggleChannel");
                out.writeUTF("local");
                out.writeUTF(p.getUniqueId().toString());
                p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (args.length > 0){
                try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                     DataOutputStream out = new DataOutputStream(b)) {
                    out.writeUTF("MaSuiteChat");
                    out.writeUTF("SendMessage");
                    out.writeUTF("local");
                    out.writeUTF(p.getUniqueId().toString());
                    out.writeUTF(Joiner.on(" ").join(args));
                    p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return false;
    }
}

