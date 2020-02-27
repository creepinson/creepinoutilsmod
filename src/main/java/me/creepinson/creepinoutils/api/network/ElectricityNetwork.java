package me.creepinson.creepinoutils.api.network;

import cofh.redstoneflux.api.IEnergyConnection;
import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import me.creepinson.creepinoutils.CreepinoUtilsMod;
import me.creepinson.creepinoutils.api.util.BlockUtils;
import me.creepinson.creepinoutils.api.util.CreepinoUtils;
import me.creepinson.creepinoutils.api.util.math.Vector3;
import net.minecraft.block.state.IBlockState;
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
    public Set<Vector3> connections = new HashSet<>();
    public final World world;
    public int transferRate = 10;

    public ElectricityNetwork(Vector3 start, World w) {
        this.startingPos = start;
        this.world = w;
    }

    @Override
    public Integer produce(Vector3... ignore) {
        Set<Vector3> avaliableEnergyTiles = this.getAcceptors();

        int stored = 0;
        int producedEnergy = 0;
        if (!avaliableEnergyTiles.isEmpty()) {
            if (transferRate > 0) {

                for (Vector3 vec : avaliableEnergyTiles) {
                    TileEntity tileEntity = getWorld().getTileEntity(vec.toBlockPos());
                    Class<INetworkAcceptor<Integer>> type = (Class<INetworkAcceptor<Integer>>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                    for (Vector3 pv : getProducers()) {
                        TileEntity producer = world.getTileEntity(pv.toBlockPos());
                        if (producer instanceof INetworkProducer) {
                            if (((INetworkProducer) producer).canProduce(null)) {
                                stored = (int) ((INetworkProducer) producer).getStored();
                                producedEnergy = (int) ((INetworkProducer) producer).produce(transferRate);
                            }
                        }

                        int energyToSend = producedEnergy;

                        if (type.isInstance(tileEntity) && !Arrays.asList(ignore).contains(vec)) {
                            INetworkAcceptor<Integer> electricalTile = (INetworkAcceptor) tileEntity;

                            stored -= electricalTile.receive(null, energyToSend, false);
                        } else if (tileEntity instanceof IEnergyStorage) {
                            stored -= ((IEnergyStorage) tileEntity).receiveEnergy(energyToSend, true);
                        } else if (tileEntity instanceof IEnergyReceiver) {
                            // TODO: implement redstone flux api check to make sure the RF dependency exists and is loaded
                            ((IEnergyReceiver) tileEntity).receiveEnergy(null, energyToSend, true);
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
    /*
    @Override
    public Integer getRequest(Vector3... ignoreTiles) {
        List<Float> requests = new ArrayList<Float>();

        Iterator<Vector3> it = this.getAcceptors().iterator();

        while (it.hasNext()) {
            Vector3 vec = it.next();
            TileEntity tileEntity = getWorld().getTileEntity(vec.toBlockPos());
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
    }*/

    /**
     * @return Returns all acceptors in this electricity network.
     */

    public Set<Vector3> getAcceptors() {
        return this.connections.stream().filter(a -> (a instanceof INetworkAcceptor && ((INetworkAcceptor) a).canAccept(EnumFacing.NORTH)) || (a instanceof IEnergyStorage && ((IEnergyStorage) a).canReceive()) || (a instanceof IEnergyReceiver && ((IEnergyReceiver) a).canConnectEnergy(EnumFacing.NORTH))).collect(Collectors.toSet());
    }

    /**
     * @return Returns all producers in this electricity network.
     */

    public Set<Vector3> getProducers() {
        // TODO: implement enumfacing side
        return this.connections.stream().filter(a -> (a instanceof INetworkProducer && ((INetworkProducer) a).canProduce(EnumFacing.NORTH)) || (a instanceof IEnergyStorage && ((IEnergyStorage) a).canExtract()) || (a instanceof IEnergyProvider && ((IEnergyProvider) a).canConnectEnergy(EnumFacing.NORTH))).collect(Collectors.toSet());
    }

    public void updateConnectedBlocks() {
        Iterator<Vector3> it = getConnections().iterator();
        IBlockState state = world.getBlockState(startingPos.toBlockPos());
        world.notifyBlockUpdate(startingPos.toBlockPos(), state, state, 3);

        while (it.hasNext()) {
            Vector3 vec = it.next();

            IBlockState s = world.getBlockState(vec.toBlockPos());
            world.markBlockRangeForRenderUpdate(vec.toBlockPos(), vec.toBlockPos());
            world.notifyBlockUpdate(vec.toBlockPos(), s, s, 3);
            world.scheduleBlockUpdate(vec.toBlockPos(), s.getBlock(), 0, 0);
        }
    }

    /**
     * This function is called to refresh all in this network
     */
    @Override
    public void refresh() {
        refreshConnections();
        try {
            Iterator<Vector3> it = this.connections.iterator();

            while (it.hasNext()) {
                Vector3 vec = it.next();
                TileEntity conductor = world.getTileEntity(vec.toBlockPos());
                if (conductor instanceof INetworkTile) {
                    if (conductor == null) {
                        it.remove();
                    } else if (((TileEntity) conductor).isInvalid()) {
                        it.remove();
                    } else {
                        ((INetworkTile) conductor).setNetwork(this);
                    }
                }
            }
            updateConnectedBlocks();
        } catch (Exception e) {
            CreepinoUtilsMod.getInstance().getLogger().error(getClass().getSimpleName() + ": Failed to refresh connections.");
            e.printStackTrace();
        }

    }

    @Override
    public void refreshConnections() {
        this.connections.clear();
        TileEntity tile = world.getTileEntity(startingPos.toBlockPos());
        if (tile != null && !(tile.isInvalid()) && tile instanceof INetworkTile) {
            this.connections = BlockUtils.getTiles(world, startingPos, INetworkTile.class, IEnergyStorage.class, IEnergyConnection.class);
            if (CreepinoUtilsMod.getInstance().isDebug()) {
                if (!world.isRemote) {
//                    CreepinoUtilsMod.getInstance().getLogger().info("Current Connections in ElectricityNetwork[" + this.hashCode() + "]: "); // TODO: create network identification method!
//                    this.connections.forEach(c -> CreepinoUtilsMod.getInstance().getLogger().info(c.toString()));
                    CreepinoUtilsMod.getInstance().getLogger().info(this.toString());
                }
            }

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
    public void split(INetworkTile splitPoint) {
        if (splitPoint instanceof TileEntity) {
            this.getConnections().remove(splitPoint);

            /**
             * Loop through the connected blocks and attempt to see if there are connections between
             * the two points elsewhere.
             */
            Set<BlockPos> connectedBlocks = CreepinoUtils.searchForTileClass(world, ((TileEntity) splitPoint).getPos(), IEnergyStorage.class, INetworkTile.class);

            for (BlockPos v : connectedBlocks) {
                TileEntity connectedBlockA = world.getTileEntity(v);

                if (connectedBlockA instanceof INetworkTile) {
                    /**
                     * The connections A and B are not connected anymore. Give both of
                     * them a new network.
                     */
                    INetwork newNetwork = new ElectricityNetwork(startingPos, world);
                    TileEntity nodeTile = world.getTileEntity(v);

                    if (nodeTile instanceof INetworkTile) {
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
    public Set<Vector3> getConnections() {
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