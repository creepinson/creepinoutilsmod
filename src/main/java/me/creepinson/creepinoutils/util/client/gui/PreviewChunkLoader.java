package me.creepinson.creepinoutils.util.client.gui;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import me.creepinson.creepinoutils.api.util.math.Vector;
import me.creepinson.creepinoutils.util.VectorUtils;
import me.creepinson.creepinoutils.util.world.ChunkUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;

import java.util.ArrayList;
import java.util.List;

/**
 * Used by {@link BlockPreviewRenderer} for retrieving loaded chunks that can be rendered.
 * To use this class, simply create an isntance of it and then call
 */
public class PreviewChunkLoader {
    public final World world;

    private final Chunk blankChunk;
    private final Long2ObjectMap<List<BlockPos>> toRender = new Long2ObjectOpenHashMap<>(8192);

    public PreviewChunkLoader(World worldIn) {
        this.blankChunk = new EmptyChunk(worldIn, 0, 0);
        this.world = worldIn;
    }

    public Chunk loadChunk(BlockPos chunkPos) {
        Chunk chunk = world.getChunk(chunkPos);
        List<BlockPos> toRender = new ArrayList<>();
        for (int x = 15; x >= 0; x--) {
            for (int y = 15; y >= 0; y--) {
                for (int z = 15; z >= 0; z--) {
                    BlockPos pos = new BlockPos(chunk.x * 16 + x, y + 40, z);
                    IBlockState state = chunk.getBlockState(pos);
                    if (state.getBlock() == Blocks.AIR) {
                        continue;
                    }

                    if (state.getBlock() == Blocks.BARRIER) {
                        continue;
                    }

                    toRender.add(pos);
                }
            }
        }

        this.toRender.put(ChunkPos.asLong(chunk.x, chunk.z), toRender);
        return chunk;
    }

    public Chunk loadChunk(Vector chunkPos) {
        return this.loadChunk(VectorUtils.toBlockPos(chunkPos));
    }

    public Chunk loadChunk(int chunkX, int chunkZ) {
        Chunk chunk = world.getChunk(chunkX, chunkZ);
        List<BlockPos> toRender = new ArrayList<>();
        for (int x = 15; x >= 0; x--) {
            for (int y = 15; y >= 0; y--) {
                for (int z = 15; z >= 0; z--) {
                    BlockPos pos = new BlockPos(chunk.x * 16 + x, y + 40, z);
                    IBlockState state = chunk.getBlockState(pos);
                    if (state.getBlock() == Blocks.AIR) {
                        continue;
                    }

                    if (state.getBlock() == Blocks.BARRIER) {
                        continue;
                    }

                    toRender.add(pos);
                }
            }
        }

        this.toRender.put(ChunkPos.asLong(chunk.x, chunk.z), toRender);
        return chunk;
    }

    public Chunk loadChunkFromNBT(NBTTagCompound tag) {
        Chunk chunk = ChunkUtils.readChunkFromNBT(world, tag);
        chunk.markLoaded(true);

        List<BlockPos> toRender = new ArrayList<>();
        for (int x = 15; x >= 0; x--) {
            for (int y = 15; y >= 0; y--) {
                for (int z = 15; z >= 0; z--) {
                    BlockPos pos = new BlockPos(chunk.x * 16 + x, y + 40, z);
                    IBlockState state = chunk.getBlockState(pos);
                    if (state.getBlock() == Blocks.AIR) {
                        continue;
                    }

                    if (state.getBlock() == Blocks.BARRIER) {
                        continue;
                    }

                    toRender.add(pos);
                }
            }
        }

        this.toRender.put(ChunkPos.asLong(chunk.x, chunk.z), toRender);
        return chunk;
    }

    /**
     * @param x
     * @param z
     * @return The chunk that has been loaded for rendering.
     */
    public List<BlockPos> getRenderListForChunk(int x, int z) {
        loadChunk(x, z);
        return this.toRender.get(ChunkPos.asLong(x, z));
    }
}