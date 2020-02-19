package me.creepinson.mod.api.upgrade;

import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @author Creepinson http://gitlab.com/creepinson
 * Project creepinoutils
 **/
public interface IUpgradeable {
    public boolean upgrade(UpgradeInfo info);

    public ItemStack downgrade(Upgrade upgrade);

    public boolean canUpgrade();

    public List<UpgradeInfo> getUpgrades();
}
