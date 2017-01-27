package online.pizzacrust.netman.api;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;

/**
 * Represents the packet format of packets.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public interface PacketFormat {

    /**
     * Identifies a packet class from a set of bytes.
     * @param dataInputStream
     * @return
     */
    Class<?> identifyPacketClass(DataInputStream dataInputStream);

    /**
     * Formats a packet according to the packet format.
     * @param byteArrayOutputStream
     * @return
     */
    ByteArrayOutputStream formatPacket(FormatInfo formatInfo, ByteArrayOutputStream
            byteArrayOutputStream);

    /**
     * Represents data about the format for {@link #formatPacket(FormatInfo, ByteArrayOutputStream)}
     *
     * @since 1.0-SNAPSHOT
     * @author PizzaCrust
     */
    interface FormatInfo {

    }

}