package online.pizzacrust.netman.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a provider for packet senders.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public class PacketSenderProvider {

    private static final List<PacketSender> PACKET_SENDERS = new ArrayList<>();

    public static void registerPacketSender(PacketSender sender) {
        PACKET_SENDERS.add(sender);
    }

    public static Optional<PacketSender> getPacketSender(Class<? extends PacketSender>
                                                                 senderClass) {
        for (PacketSender sender : PACKET_SENDERS) {
            if (sender.getClass() == senderClass) {
                return Optional.of(sender);
            }
        }
        return Optional.empty();
    }

}