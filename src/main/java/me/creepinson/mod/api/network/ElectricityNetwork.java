package me.creepinson.mod.api.network;

import me.creepinson.mod.CreepinoUtilsMod;
import me.creepinson.mod.api.util.CreepinoUtils;
import me.creepinson.mod.api.util.math.Vector3;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public class ElectricityNetwork implements INetwork<Integer> {
    private final Vector3 startingPos;
    public Set<BlockPos> connections = new HashSet<>();
    public final World world;

    public ElectricityNetwork(Vector3 start, World w) {
        this.startingPos = start;
        this.world = w;
    }

    @Override
    public Integer produce(INetworkProducer producer, Vector3... ignore) {
        int stored = (int) producer.getStored();

        Set<BlockPos> avaliableEnergyTiles = this.getAcceptors();

        if (!avaliableEnergyTiles.isEmpty()) {
            final int totalEnergyRequest = this.getRequest(ignore);
            CreepinoUtilsMod.getInstance().getLogger().info(totalEnergyRequest);
            if (totalEnergyRequest > 0) {
                for (BlockPos vec : avaliableEnergyTiles) {
                    TileEntity tileEntity = getWorld().getTileEntity(vec);
                    Class<INetworkAcceptor<Integer>> type = (Class<INetworkAcceptor<Integer>>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                    if (type.isInstance(tileEntity) && !Arrays.asList(ignore).contains(vec)) {
                        INetworkAcceptor electricalTile = (INetworkAcceptor) tileEntity;
                        // TODO: Fix Direction
                        int energyToSend = stored / totalEnergyRequest;

                        if (energyToSend > 0) {

                            // Calculate energy loss caused by resistance

                            // TODO: Fix unknown direction!
                            stored -= ((INetworkAcceptor<Integer>) tileEntity).receive(EnumFacing.NORTH, energyToSend, true);
                        }
                    }
                }
            }
        }
        return stored;
    }

    /**
     * @return How much electricity this network needs.
     */
    @Override
    public Integer getRequest(Vector3... ignoreTiles) {
        List<Float> requests = new ArrayList<Float>();

        Iterator<BlockPos> it = this.getAcceptors().iterator();

        while (it.hasNext()) {
            BlockPos vec = it.next();
            TileEntity tileEntity = getWorld().getTileEntity(vec);
            if (Arrays.asList(ignoreTiles).contains(vec)) {
                continue;
            }

            if (tileEntity instanceof INetworkAcceptor) {
                if (!tileEntity.isInvalid()) {
//                    if (tileEntity.getWorld().getTileEntity(tileEntity.getPos()) == tileEntity) {
                    requests.add((Float) ((INetworkAcceptor) tileEntity).getRequest(EnumFacing.NORTH));
                    continue;
//                    }
                }
            } else if (tileEntity instanceof IEnergyStorage) {
                if (!tileEntity.isInvalid()) {
                    requests.add((float) ((IEnergyStorage) tileEntity).getMaxEnergyStored());
                    continue;
                }
            }
            it.remove();
        }
        int r = 0;
        for (float f : requests) {
            r += f;
        }

        return r;
    }

    /**
     * @return Returns all acceptors in this electricity network.
     */

    public Set<BlockPos> getAcceptors() {
        return this.connections.stream().filter(a -> a instanceof INetworkAcceptor || (a instanceof IEnergyStorage && ((IEnergyStorage) a).canReceive())).collect(Collectors.toSet());
    }

    /**
     * @return Returns all producers in this electricity network.
     */

    public Set<BlockPos> getProducers() {
        return this.connections.stream().filter(a -> a instanceof INetworkProducer || (a instanceof IEnergyStorage && ((IEnergyStorage) a).canExtract())).collect(Collectors.toSet());
    }

    /**
     * This function is called to refresh all in this network
     */
    @Override
    public void refresh() {
        refreshConnections();
        try {
            Iterator<BlockPos> it = this.connections.iterator();

            while (it.hasNext()) {
                BlockPos vec = it.next();
                TileEntity conductor = world.getTileEntity(vec);
                if (conductor instanceof INetworkedTile) {
                    if (conductor == null) {
                        it.remove();
                    } else if (((TileEntity) conductor).isInvalid()) {
                        it.remove();
                    } else {
                        ((INetworkedTile) conductor).setNetwork(this);
                    }
                }
            }
        } catch (Exception e) {
            CreepinoUtilsMod.getInstance().getLogger().error(getClass().getSimpleName() + ": Failed to refresh connections.");
            e.printStackTrace();
        }
    }

    @Override
    public void refreshConnections() {
        this.connections.clear();
        TileEntity tile = world.getTileEntity(startingPos.toBlockPos());
        if (tile != null && !(tile.isInvalid())) {
            this.connections = CreepinoUtils.searchForTileClass(world, tile.getPos(), IEnergyStorage.class, INetworkedTile.class);

        }
    }

    @Override
    public void merge(INetwork<Integer> network) {
        if (network != null && network != this) {
            ElectricityNetwork newNetwork = new ElectricityNetwork(startingPos, this.getWorld());
            newNetwork.connections.addAll(this.connections);
            newNetwork.refresh();
        }
    }

    @Override
    public void split(INetworkedTile splitPoint) {
        if (splitPoint instanceof TileEntity) {
            this.getConnections().remove(splitPoint);

            /**
             * Loop through the connected blocks and attempt to see if there are connections between
             * the two points elsewhere.
             */
            Set<BlockPos> connectedBlocks = CreepinoUtils.searchForTileClass(world, ((TileEntity)splitPoint).getPos(), IEnergyStorage.class, INetworkedTile.class);

            for (BlockPos v : connectedBlocks) {
                TileEntity connectedBlockA = world.getTileEntity(v);

                if (connectedBlockA instanceof INetworkedTile) {
                    /**
                     * The connections A and B are not connected anymore. Give both of
                     * them a new network.
                     */
                    INetwork newNetwork = new ElectricityNetwork(startingPos, world);
                    TileEntity nodeTile = world.getTileEntity(v);

                    if (nodeTile instanceof INetworkedTile) {
                        if (nodeTile != splitPoint) {
                            newNetwork.getConnections().add(connectedBlockA);
                        }
                    }

                    newNetwork.refresh();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "ElectricityNetwork[" + this.hashCode() + "|Producers}:" + this.getProducers().size() + "|Acceptors:" + this.getAcceptors().size() + "]";
    }

    @Override
    public Set<BlockPos> getConnections() {
        return this.connections;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public NBTTagCompound serialize() {
        NBTTagCompound tag = new NBTTagCompound();
        return tag;
    }

    @Override
    public void deserialize(NBTTagCompound compound) {

    }

}