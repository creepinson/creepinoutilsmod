package me.creepinson.creepinoutils.api.upgrade;

import me.creepinson.creepinoutils.api.util.text.EnumColor;
import me.creepinson.creepinoutils.api.util.text.LangUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants.NBT;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Upgrade {
    public static final HashMap<String, Upgrade> UPGRADES = new HashMap<>();

    public static final Upgrade SPEED = new Upgrade("speed", 25, EnumColor.RED);

    public static final Upgrade ENERGY = new Upgrade("energy", 25, EnumColor.BRIGHT_GREEN);
    public static final Upgrade FILTER = new Upgrade("filter", Integer.MAX_VALUE, EnumColor.DARK_AQUA);
    public static final Upgrade ANCHOR = new Upgrade("anchor", 1, EnumColor.DARK_GREEN);

    static {
        UPGRADES.put(SPEED.name, SPEED);
    }

    private String name;
    private int maxStack;
    private EnumColor color;

    Upgrade(String s, int max, EnumColor c) {
        name = s;
        maxStack = max;
        color = c;
    }

    public static Map<Upgrade, Integer> buildMap(@Nullable NBTTagCompound nbtTags) {
        Map<Upgrade, Integer> upgrades = new HashMap<>();
        if (nbtTags != null) {
            if (nbtTags.hasKey("upgrades")) {
                NBTTagList list = nbtTags.getTagList("upgrades", NBT.TAG_COMPOUND);
                for (int tagCount = 0; tagCount < list.tagCount(); tagCount++) {
                    NBTTagCompound compound = list.getCompoundTagAt(tagCount);
                    Upgrade upgrade = Upgrade.getUpgradesArray()[compound.getInteger("type")];
                    upgrades.put(upgrade, compound.getInteger("amount"));
                }
            }
        }
        return upgrades;
    }

    public static Upgrade[] getUpgradesArray() {
        return UPGRADES.values().toArray(new Upgrade[0]);
    }

    public static void saveMap(Map<Upgrade, Integer> upgrades, NBTTagCompound nbtTags) {
        NBTTagList list = new NBTTagList();
        for (Entry<Upgrade, Integer> entry : upgrades.entrySet()) {
            list.appendTag(getTagFor(entry.getKey(), entry.getValue()));
        }
        nbtTags.setTag("upgrades", list);
    }

    public static NBTTagCompound getTagFor(Upgrade upgrade, int amount) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("type", upgrade.name);
        compound.setInteger("amount", amount);
        return compound;
    }

    public String getName() {
        return LangUtils.localize("upgrade." + name);
    }

    public String getDescription() {
        return LangUtils.localize("upgrade." + name + ".desc");
    }

    public int getMax() {
        return maxStack;
    }

    public EnumColor getColor() {
        return color;
    }

    public boolean canMultiply() {
        return getMax() > 1;
    }

    public List<String> getInfo(TileEntity tile) {
        List<String> ret = new ArrayList<>();
        if (tile instanceof IUpgradeable) {
            if (tile instanceof IUpgradeInfoHandler) {
                return ((IUpgradeInfoHandler) tile).getInfo(this);
            }
        }
        return ret;
    }

    public interface IUpgradeInfoHandler {

        List<String> getInfo(Upgrade upgrade);
    }
}