package me.creepinson.creepinoutils.api.upgrade;

import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public interface IUpgradeable {
    boolean upgrade(UpgradeInfo info);

    ItemStack removeUpgrade(Upgrade upgrade);

    boolean canUpgrade();

    List<UpgradeInfo> getStoredUpgrades();

}
