package me.creepinson.creepinoutils.util.upgrade;

import me.creepinson.creepinoutils.util.text.EnumColor;
import me.creepinson.creepinoutils.util.text.LangUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
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

    public static Map<Upgrade, Integer> buildMap(@Nullable CompoundNBT nbtTags) {
        Map<Upgrade, Integer> upgrades = new HashMap<>();
        if (nbtTags != null) {
            if (nbtTags.contains("upgrades")) {
                ListNBT list = nbtTags.getList("upgrades", NBT.TAG_COMPOUND);
                for (int size = 0; size < list.size(); size++) {
                    CompoundNBT compound = list.getCompound(size);
                    Upgrade upgrade = Upgrade.getUpgradesArray()[compound.getInt("type")];
                    upgrades.put(upgrade, compound.getInt("amount"));
                }
            }
        }
        return upgrades;
    }

    public static Upgrade[] getUpgradesArray() {
        return UPGRADES.values().toArray(new Upgrade[0]);
    }

    public static void saveMap(Map<Upgrade, Integer> upgrades, CompoundNBT nbtTags) {
        ListNBT list = new ListNBT();
        for (Entry<Upgrade, Integer> entry : upgrades.entrySet()) {
            list.add(getTagFor(entry.getKey(), entry.getValue()));
        }
        nbtTags.put("upgrades", list);
    }

    public static CompoundNBT getTagFor(Upgrade upgrade, int amount) {
        CompoundNBT compound = new CompoundNBT();
        compound.putString("type", upgrade.name);
        compound.putInt("amount", amount);
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