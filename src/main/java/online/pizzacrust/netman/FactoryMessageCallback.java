package online.pizzacrust.netman;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;

import online.pizzacrust.netman.api.PacketHandler;

public class FactoryMessageCallback implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("netman")) {
            PacketHandler.handleRetrievePacket(new ByteArrayInputStream(message));
        }
    }
}
