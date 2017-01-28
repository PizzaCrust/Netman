package online.pizzacrust.netman.api.bukkit;

import org.bukkit.entity.Player;

import online.pizzacrust.netman.api.User;

/**
 * Represents a Bukkit player.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public class BukkitUser implements User {

    private final Player player;

    public BukkitUser(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}