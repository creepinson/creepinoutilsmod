package me.creepinson.creepinoutils.base;

import me.creepinson.creepinoutils.api.network.INetworkTile;
import me.creepinson.creepinoutils.api.util.math.Vector3;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public abstract class TileMultiBlock3x3 extends TileMultiBlock {
    public void resetStructure() {
        int xCoord = getPosition().intX();
        int yCoord = getPosition().intY();
        int zCoord = getPosition().intZ();
        for (int x = xCoord - 1; x < xCoord + 2; x++)
            for (int y = yCoord; y < yCoord + 3; y++)
                for (int z = zCoord - 1; z < zCoord + 2; z++) {
                    TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                    if (tile != null && (tile instanceof TileMultiBlock))
                        ((TileMultiBlock) tile).reset();
                }
    }

    @Override
    public void onNeighborChange(Vector3 v) {
        TileEntity tile = v.getTileEntity(world);
        if (tile != null && tile instanceof TileMultiBlock) {
            TileMultiBlock multiBlock = (TileMultiBlock) tile;
            if (multiBlock.hasMaster()) {
                if (multiBlock.isMaster()) {
                    if (!multiBlock.checkMultiBlockForm())
                        multiBlock.resetStructure();
                } else {
                    if (!multiBlock.checkForMaster())
                        multiBlock.reset();
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (!world.isRemote) {
            if (hasMaster()) {
                if (isMaster()) {
                    // Put stuff you want the multiblock to do here!
                    updateMultiblock();
                }
            } else {
                // Constantly check if structure is formed until it is.
                if (checkMultiBlockForm())
                    setupStructure();
            }
        }
    }

    protected abstract void updateMultiblock();

    public boolean checkMultiBlockForm() {
        int i = 0;
        int xCoord = getPosition().intX();
        int yCoord = getPosition().intY();
        int zCoord = getPosition().intZ();

        // Scan a 3x3x3 area, starting with the bottom left corner
        for (int x = xCoord - 1; x < xCoord + 2; x++)
            for (int y = yCoord; y < yCoord + 3; y++)
                for (int z = zCoord - 1; z < zCoord + 2; z++) {
                    TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                    // Make sure tile isn't null, is an instance of the same Tile, and isn't already
                    // a part of a multiblock
                    if (tile != null && (tile instanceof TileMultiBlock)) {
                        if (this.isMaster()) {
                            if (((TileMultiBlock) tile).hasMaster())
                                i++;
                        } else if (!((TileMultiBlock) tile).hasMaster())
                            i++;
                    }
                }
        // check if there are 26 blocks present ((3*3*3) - 1) and check that center
        // block is empty
        return i > 25 && world.isAirBlock(new BlockPos(xCoord, yCoord + 1, zCoord));
    }

    public void setupStructure() {
        int xCoord = getPosition().intX();
        int yCoord = getPosition().intY();
        int zCoord = getPosition().intZ();
        for (int x = xCoord - 1; x < xCoord + 2; x++)
            for (int y = yCoord; y < yCoord + 3; y++)
                for (int z = zCoord - 1; z < zCoord + 2; z++) {
                    TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                    // Check if block is bottom center block
                    boolean master = (x == xCoord && y == yCoord && z == zCoord);
                    if (tile != null && (tile instanceof TileMultiBlock)) {
                        ((TileMultiBlock) tile).getMasterPosition().x = xCoord;
                        ((TileMultiBlock) tile).getMasterPosition().y = yCoord;
                        ((TileMultiBlock) tile).getMasterPosition().z = zCoord;
                        ((TileMultiBlock) tile).setHasMaster(true);
                        ((TileMultiBlock) tile).setIsMaster(master);
                    }
                }
    }
}