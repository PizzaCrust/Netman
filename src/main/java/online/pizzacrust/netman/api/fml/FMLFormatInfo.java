package online.pizzacrust.netman.api.fml;

import online.pizzacrust.netman.api.PacketFormat;

public class FMLFormatInfo implements PacketFormat.FormatInfo {

    private final int discriminator;

    public FMLFormatInfo(int discriminator) {
        this.discriminator = discriminator;
    }

    public int getDiscriminator() {
        return discriminator;
    }
}
