package online.pizzacrust.netman.api;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles retrieving packets.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public class PacketHandler {

    private static final List<PacketListener> LISTENER_LIST = new ArrayList<>();

    public static void registerPacketListener(PacketListener listener) {
        LISTENER_LIST.add(listener);
    }

    public static void handleRetrievePacket(User user, ByteArrayInputStream byteArrayInputStream) {
        Optional<Class<?>> packetClass = Optional.empty();
        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
        for (PacketFormat packetFormat : PacketFormatProvider.getRegisteredPacketFormats()) {
            Class<?> identifiedClass = packetFormat.identifyPacketClass(dataInputStream);
            if (identifiedClass != null) {
                packetClass = Optional.of(identifiedClass);
            }
        }
        ClassSerializer classSerializer = new ClassSerializer();
        packetClass.ifPresent((clazz) -> {
            Object packet = classSerializer.deserialize(dataInputStream, clazz);
            if (packet != null) {
                LISTENER_LIST.forEach((listener) -> listener.onRetrieve(user, packet));
            }
        });
    }

}
