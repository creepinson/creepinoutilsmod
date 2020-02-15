package com.draco18s.hardlib.api.interfaces;

import com.draco18s.hardlib.api.internal.BlockWrapper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IOreData {

    /**
     * Add ore count to tracker
     *
     * @param world
     * @param pos
     * @param ore
     * @param count
     */
    public void putOreData(World world, BlockPos pos, BlockWrapper ore, int count);

    /**
     * Get ore count from tracker
     *
     * @param world
     * @param pos
     * @param ore
     */
    public int getOreData(World world, BlockPos pos, BlockWrapper ore);

    /**
     * Modify the tracked value
     *
     * @param world
     * @param pos
     * @param ore
     */
    public void adjustOreData(World world, BlockPos pos, BlockWrapper ore, int amount);
}
