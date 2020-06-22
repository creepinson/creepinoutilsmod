package me.creepinson.creepinoutils.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTileData extends BlockData {

    private TileEntity tileEntity;

    public TileEntity getTileEntity() {
        return tileEntity;
    }

    public void setTileEntity(TileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    public BlockTileData(World world) {
        super(world);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        if (tileEntity != null && !tileEntity.isRemoved()) {
            nbt.setTag("tile", tileEntity.serializeNBT());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        if (nbt.hasKey("tile")) {
            this.tileEntity = TileEntity.create(getWorld(), nbt.getCompoundTag("tile"));
        }
    }
}
