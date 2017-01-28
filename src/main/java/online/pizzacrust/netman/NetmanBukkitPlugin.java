package online.pizzacrust.netman;

import org.bukkit.plugin.java.JavaPlugin;

import online.pizzacrust.netman.api.PacketSenderProvider;
import online.pizzacrust.netman.api.bukkit.BukkitPacketSender;

public class NetmanBukkitPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        BukkitPacketSender.SOURCE = this;
        getLogger().info("Registering required Bukkit packet sender...");
        PacketSenderProvider.registerPacketSender(new BukkitPacketSender());
        getLogger().info("Registering required Bukkit packet handler...");
        getServer().getMessenger().registerIncomingPluginChannel(this, "netman", new FactoryMessageCallback());
        getServer().getMessenger().registerOutgoingPluginChannel(this, "netman");
    }
    @Override
    public void onDisable() {
        getLogger().info("Netman has been disabled!");
    }
}
