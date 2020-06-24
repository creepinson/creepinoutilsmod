package me.creepinson.creepinoutils.util.upgrade;

import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @author Theo Paris https://theoparis.com
 * Project creepinoutils
 **/
public interface IUpgradeable {
    boolean upgrade(UpgradeInfo info);

    ItemStack removeUpgrade(Upgrade upgrade);

    boolean canUpgrade();

    List<UpgradeInfo> getStoredUpgrades();

}
