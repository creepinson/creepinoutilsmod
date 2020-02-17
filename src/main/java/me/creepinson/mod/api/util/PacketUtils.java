package me.creepinson.mod.api.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * Mekanism packet handler. As always, use packets sparingly!
 *
 * @author AidanBrady
 */
public class PacketUtils {
    public static void writeString(ByteBuf output, String s) {
        ByteBufUtils.writeUTF8String(output, s);
    }

    public static String readString(ByteBuf input) {
        return ByteBufUtils.readUTF8String(input);
    }

    public static void writeStack(ByteBuf output, ItemStack stack) {
        ByteBufUtils.writeItemStack(output, stack);
    }

    public static ItemStack readStack(ByteBuf input) {
        return ByteBufUtils.readItemStack(input);
    }

    public static void writeNBT(ByteBuf output, NBTTagCompound nbtTags) {
        ByteBufUtils.writeTag(output, nbtTags);
    }

    public static NBTTagCompound readNBT(ByteBuf input) {
        return ByteBufUtils.readTag(input);
    }

}
