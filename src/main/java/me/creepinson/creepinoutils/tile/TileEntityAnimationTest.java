package me.creepinson.creepinoutils.tile;

import me.creepinson.creepinoutils.CreepinoUtilsMod;
import me.creepinson.creepinoutils.base.InventoryNetworkTileEntity;
import me.creepinson.creepinoutils.util.upgrade.UpgradeInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author Creepinson http://gitlab.com/creepinson
 **/

public class TileEntityAnimationTest extends InventoryNetworkTileEntity {
    private ItemStackHandler items = new ItemStackHandler(1);

    public TileEntityAnimationTest(TileEntityType<?> type) {
        super(type, 1);
    }

    public void onClick() {
        setActive(!isActive());
    }

    @Override
    public void setActive(boolean value) {
        super.setActive(value);
        if (world != null && CreepinoUtilsMod.getInstance().isDebug() && !world.isRemote) {
            CreepinoUtilsMod.getInstance().getLogger().info("New Active Value: " + this.isActive());
        }
    }

    @Override
    protected IItemHandler getItemHandler() {
        return this.items;
    }

    @Override
    public boolean canConnectTo(IWorld world, BlockPos pos, Direction f) {
        return super.canConnectTo(world, pos, f);
    }

    @Override
    public boolean upgrade(UpgradeInfo info) {
        return canUpgrade();
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void tick() {
        this.items.setStackInSlot(0, new ItemStack(Items.APPLE, 1));
    }

    @Override
    public void onNeighborChange(BlockPos pos) {
    }
}