package me.creepinson.creepinoutils.util.upgrade;

import net.minecraft.item.ItemStack;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public class UpgradeInfo {
    public final Upgrade upgrade;
    public final ItemStack upgradeItem;

    public UpgradeInfo(Upgrade upgrade, ItemStack upgradeItem) {
        this.upgrade = upgrade;
        this.upgradeItem = upgradeItem;
    }
}
