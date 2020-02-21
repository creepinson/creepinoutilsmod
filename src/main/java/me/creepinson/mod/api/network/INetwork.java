package me.creepinson.mod.api.network;

import me.creepinson.mod.api.INetworkedTile;
import me.creepinson.mod.api.util.math.Vector3;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.HashMap;
import java.util.HashSet;

public interface INetwork<T> {
    HashMap<Vector3, EnumFacing> getConnections();
    World getWorld();
    <T> T produce(INetworkedTile producer, Vector3... ignore);
    T getRequest(Vector3... ignore);
}
