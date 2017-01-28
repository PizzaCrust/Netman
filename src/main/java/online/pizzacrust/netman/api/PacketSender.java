package online.pizzacrust.netman.api;

/**
 * Represents a sender of packets.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public interface PacketSender {

    /**
     * Sends a packet to the user.
     * @param user
     * @param object
     */
    void sendPacket(User user, Object object, PacketFormat.FormatInfo formatInfo, Class<? extends
            PacketFormat> format);

}
