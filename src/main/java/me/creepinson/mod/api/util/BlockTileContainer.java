package me.creepinson.mod.api.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTileContainer extends BlockContainer {

    private TileEntity tileEntity;

    public TileEntity getTileEntity() {
        return tileEntity;
    }

    public void setTileEntity(TileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    public BlockTileContainer(World world) {
        super(world);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = super.serializeNBT();
        if (tileEntity != null && !tileEntity.isInvalid()) {
            nbt.setTag("tile", tileEntity.serializeNBT());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        if (nbt.hasKey("tile")) {
            this.tileEntity = TileEntity.create(getWorld(), nbt.getCompoundTag("tile"));
        }
    }
}
