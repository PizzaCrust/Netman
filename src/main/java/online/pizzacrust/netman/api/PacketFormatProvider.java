package online.pizzacrust.netman.api;

import java.util.ArrayList;
import java.util.List;

import online.pizzacrust.netman.api.fml.FMLSimplePacketFormat;

/**
 * Represents a provider of packet formats.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public class PacketFormatProvider {

    private static final List<PacketFormat> REGISTERED_PACKET_FORMATS =
            createDefaultFormatRegistry();

    private static List<PacketFormat> createDefaultFormatRegistry() {
        ArrayList<PacketFormat> packetFormats = new ArrayList<>();
        packetFormats.add(new FMLSimplePacketFormat());
        return packetFormats;
    }

    public static void registerPacketFormat(PacketFormat packetFormat) {
        REGISTERED_PACKET_FORMATS.add(packetFormat);
    }

    public static List<PacketFormat> getRegisteredPacketFormats() {
        return REGISTERED_PACKET_FORMATS;
    }

}
