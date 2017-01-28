package online.pizzacrust.netman.api.fml;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import online.pizzacrust.netman.api.PacketFormat;
import online.pizzacrust.netman.api.PacketSender;
import online.pizzacrust.netman.api.PacketSenderProvider;
import online.pizzacrust.netman.api.User;

/**
 * Represents a implementation of FML's packet format for the SimpleNetworkingImplementation.
 *
 * @since 1.0-SNAPSHOT
 * @author PizzaCrust
 */
public class FMLSimplePacketFormat implements PacketFormat {

    private static final Map<Integer, Class<?>> DISCRIMINATOR_TO_CLASS = new HashMap<>();

    public static void registerFMLPacket(int discriminator, Class<?> clazz) {
        DISCRIMINATOR_TO_CLASS.put(discriminator, clazz);
    }

    public static void sendPacket(User user, FMLFormatInfo fmlFormatInfo, Object object, Class<?
            extends PacketSender> packetSender) {
        Optional<PacketSender> senderOpt = PacketSenderProvider.getPacketSender(packetSender);
        senderOpt.ifPresent((sender) -> sender.sendPacket(user, object, fmlFormatInfo, FMLSimplePacketFormat.class));
    }


    @Override
    public Class<?> identifyPacketClass(DataInputStream dataInputStream) {
        try {
            int discriminator = dataInputStream.readByte();
            if (DISCRIMINATOR_TO_CLASS.containsKey(discriminator)) {
                return DISCRIMINATOR_TO_CLASS.get(discriminator);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ByteArrayOutputStream formatPacket(FormatInfo formatInfo, ByteArrayOutputStream
                                                          byteArrayOutputStream) {
        if (formatInfo instanceof FMLFormatInfo) {
            FMLFormatInfo fmlFormatInfo = (FMLFormatInfo) formatInfo;
            ByteArrayOutputStream packet = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(packet);
            try {
                dataOutputStream.writeByte(fmlFormatInfo.getDiscriminator());
                dataOutputStream.write(byteArrayOutputStream.toByteArray());
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] bytes = packet.toByteArray();
            return packet;
        }
        return byteArrayOutputStream;
    }

}
