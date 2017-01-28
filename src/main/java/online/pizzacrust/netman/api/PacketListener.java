package online.pizzacrust.netman.api;

/**
 * Represents a packet listener to Netman packets.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public interface PacketListener {

    void onRetrieve(User user, Object packet);

}
