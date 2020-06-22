package me.creepinson.creepinoutils.util.world;

import me.creepinson.creepinoutils.CreepinoUtilsMod;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ChunkUtils {
    public static List<String> erroneousTiles = new ArrayList<>();
    private static Field updatePacketNBTField;

    // This is copied straight from the AnvilChunkLoader class

    /**
     * Writes the Chunk passed as an argument to the CompoundNBT also passed, using the World argument to retrieve
     * the Chunk's last update time.
     */
    public static CompoundNBT writeChunkToNBT(Chunk chunkIn, World worldIn, CompoundNBT compound) {
        compound.setInteger("xPos", chunkIn.x);
        compound.setInteger("zPos", chunkIn.z);
        compound.setLong("LastUpdate", worldIn.getTotalWorldTime());
        compound.setIntArray("HeightMap", chunkIn.getHeightMap());
        compound.setBoolean("TerrainPopulated", chunkIn.isTerrainPopulated());
        compound.setBoolean("LightPopulated", chunkIn.isLightPopulated());
        compound.setLong("InhabitedTime", chunkIn.getInhabitedTime());
        ExtendedBlockStorage[] aextendedblockstorage = chunkIn.getBlockStorageArray();
        ListNBT ListNBT = new ListNBT();
        boolean flag = worldIn.provider.hasSkyLight();


        for (ExtendedBlockStorage extendedblockstorage : aextendedblockstorage) {
            if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE) {
                CompoundNBT CompoundNBT = new CompoundNBT();
                CompoundNBT.setByte("Y", (byte) (extendedblockstorage.getYLocation() >> 4 & 255));
                byte[] abyte = new byte[4096];
                NibbleArray nibblearray = new NibbleArray();
                NibbleArray nibblearray1 = extendedblockstorage.getData().getDataForNBT(abyte, nibblearray);
                CompoundNBT.setByteArray("Blocks", abyte);
                CompoundNBT.setByteArray("Data", nibblearray.getData());

                if (nibblearray1 != null) {
                    CompoundNBT.setByteArray("Add", nibblearray1.getData());
                }

                CompoundNBT.setByteArray("BlockLight", extendedblockstorage.getBlockLight().getData());

                if (flag) {
                    CompoundNBT.setByteArray("SkyLight", extendedblockstorage.getSkyLight().getData());
                } else {
                    CompoundNBT.setByteArray("SkyLight", new byte[extendedblockstorage.getBlockLight().getData().length]);
                }

                ListNBT.appendTag(CompoundNBT);
            }
        }

        compound.setTag("Sections", ListNBT);
        compound.setByteArray("Biomes", chunkIn.getBiomeArray());
        chunkIn.setHasEntities(false);
        ListNBT ListNBT1 = new ListNBT();

        for (int i = 0; i < chunkIn.getEntityLists().length; ++i) {
            for (Entity entity : chunkIn.getEntityLists()[i]) {
                CompoundNBT CompoundNBT2 = new CompoundNBT();

                try {
                    if (entity.writeToNBTOptional(CompoundNBT2)) {
                        chunkIn.setHasEntities(true);
                        ListNBT1.appendTag(CompoundNBT2);
                    }
                } catch (Exception e) {
                    net.minecraftforge.fml.common.FMLLog.log.error("An Entity type {} has thrown an exception trying to write state. It will not be visible in compact machines. Report this to the Compact Machines author.",
                            entity.getClass().getName(), e);
                }
            }
        }

        compound.setTag("Entities", ListNBT1);

        if (updatePacketNBTField == null) {
            updatePacketNBTField = ReflectionHelper.findField(SPacketUpdateTileEntity.class, "nbt", "field_148860_e");
            updatePacketNBTField.setAccessible(true);
        }

        ListNBT ListNBT2 = new ListNBT();
        for (TileEntity tileentity : chunkIn.getTileEntityMap().values()) {
            try {
                CompoundNBT CompoundNBT3 = tileentity.writeToNBT(new CompoundNBT());

                SPacketUpdateTileEntity updatePacket = tileentity.getUpdatePacket();
                if (updatePacket != null) {
                    CompoundNBT updateData = (CompoundNBT) updatePacketNBTField.get(updatePacket);
                    CompoundNBT3.setTag("cm3_update", updateData);
                }

                ListNBT2.appendTag(CompoundNBT3);
            } catch (Exception e) {
                net.minecraftforge.fml.common.FMLLog.log.error("A TileEntity type {} has throw an exception trying to write state. It will not be visible in compact machines. Report this to the Compact Machines author.",
                        tileentity.getClass().getName(), e);
            }
        }
        compound.setTag("TileEntities", ListNBT2);


        List<NextTickListEntry> list = worldIn.getPendingBlockUpdates(chunkIn, false);
        if (list != null) {
            long j = worldIn.getTotalWorldTime();
            ListNBT ListNBT3 = new ListNBT();

            for (NextTickListEntry nextticklistentry : list) {
                CompoundNBT CompoundNBT1 = new CompoundNBT();
                ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(nextticklistentry.getBlock());
                CompoundNBT1.setString("i", resourcelocation == null ? "" : resourcelocation.toString());
                CompoundNBT1.setInteger("x", nextticklistentry.position.getX());
                CompoundNBT1.setInteger("y", nextticklistentry.position.getY());
                CompoundNBT1.setInteger("z", nextticklistentry.position.getZ());
                CompoundNBT1.setInteger("t", (int) (nextticklistentry.scheduledTime - j));
                CompoundNBT1.setInteger("p", nextticklistentry.priority);
                ListNBT3.appendTag(CompoundNBT1);
            }

            compound.setTag("TileTicks", ListNBT3);
        }

        return compound;
    }

    /**
     * Reads the data stored in the passed CompoundNBT and creates a Chunk with that data in the passed World.
     * Returns the created Chunk.
     */
    public static Chunk readChunkFromNBT(World worldIn, CompoundNBT compound) {
        int i = compound.getInteger("xPos");
        int j = compound.getInteger("zPos");
        Chunk chunk = new Chunk(worldIn, i, j);
        chunk.setHeightMap(compound.getIntArray("HeightMap"));
        chunk.setTerrainPopulated(compound.getBoolean("TerrainPopulated"));
        chunk.setLightPopulated(compound.getBoolean("LightPopulated"));

        chunk.setInhabitedTime(compound.getLong("InhabitedTime"));
        ListNBT ListNBT = compound.getTagList("Sections", 10);
        int k = 16;
        ExtendedBlockStorage[] aextendedblockstorage = new ExtendedBlockStorage[16];
        boolean flag = true;

        for (int l = 0; l < ListNBT.tagCount(); ++l) {
            CompoundNBT CompoundNBT = ListNBT.getCompoundTagAt(l);
            int i1 = CompoundNBT.getByte("Y");
            ExtendedBlockStorage extendedblockstorage = new ExtendedBlockStorage(i1 << 4, flag);
            byte[] abyte = CompoundNBT.getByteArray("Blocks");
            NibbleArray nibblearray = new NibbleArray(CompoundNBT.getByteArray("Data"));
            NibbleArray nibblearray1 = CompoundNBT.hasKey("Add", 7) ? new NibbleArray(CompoundNBT.getByteArray("Add")) : null;
            extendedblockstorage.getData().setDataFromNBT(abyte, nibblearray, nibblearray1);
            extendedblockstorage.setBlockLight(new NibbleArray(CompoundNBT.getByteArray("BlockLight")));

            if (flag) {
                extendedblockstorage.setSkyLight(new NibbleArray(CompoundNBT.getByteArray("SkyLight")));
            }

            extendedblockstorage.recalculateRefCounts();
            aextendedblockstorage[i1] = extendedblockstorage;
        }

        chunk.setStorageArrays(aextendedblockstorage);

        if (compound.hasKey("Biomes", 7)) {
            chunk.setBiomeArray(compound.getByteArray("Biomes"));
        }

        // End this method here and split off entity loading to another method
        loadEntities(worldIn, compound, chunk);

        return chunk;
    }


    public static void loadEntities(World worldIn, CompoundNBT compound, Chunk chunk) {
        ListNBT ListNBT1 = compound.getTagList("Entities", 10);

        for (int j1 = 0; j1 < ListNBT1.tagCount(); ++j1) {
            CompoundNBT CompoundNBT1 = ListNBT1.getCompoundTagAt(j1);
            AnvilChunkLoader.readChunkEntity(CompoundNBT1, worldIn, chunk);
            chunk.setHasEntities(true);
        }

        ListNBT ListNBT2 = compound.getTagList("TileEntities", 10);

        for (int k1 = 0; k1 < ListNBT2.tagCount(); ++k1) {
            CompoundNBT CompoundNBT2 = ListNBT2.getCompoundTagAt(k1);
            TileEntity tileentity = TileEntity.create(worldIn, CompoundNBT2);
            if (tileentity != null) {
                if (erroneousTiles.contains(tileentity.getClass().getName())) {
                    continue;
                }

                try {
                    tileentity.setWorld(worldIn);
                } catch (Exception e) {
                    CreepinoUtilsMod.getInstance().getLogger().warn("Unable to render tile-entity %s in a fake world. Skipping.", tileentity.getClass().getName());
                    erroneousTiles.add(tileentity.getClass().getName());
                    continue;
                }

                chunk.addTileEntity(tileentity);

                if (CompoundNBT2.hasKey("cm3_update")) {
                    CompoundNBT tag = CompoundNBT2.getCompoundTag("cm3_update");

                    try {
                        tileentity.onDataPacket(null, new SPacketUpdateTileEntity(tileentity.getPos(), 1, tag));
                    } catch (NullPointerException npe) {
                        CreepinoUtilsMod.getInstance().debug("TileEntity" + tileentity.getClass().getName() + " is unable to read data packet without a network manager instance.");
                    } catch (Exception e) {
                        CreepinoUtilsMod.getInstance().debug("TileEntity " + tileentity.getClass().getName() + "is unable to read data packet. Probably because of internally saved block positions or dimension. Report this to the Compact Machines author, not the blocks mod author!");
                    }
                }
            }
        }
    }
}
