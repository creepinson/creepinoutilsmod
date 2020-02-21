package me.creepinson.mod.api.network;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

import me.creepinson.mod.api.network.path.Pathfinder;
import me.creepinson.mod.api.network.path.PathfinderChecker;
import me.creepinson.mod.api.util.CreepinoUtils;
import me.creepinson.mod.api.util.math.Vector3;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.FMLLog;

public class ElectricityNetwork implements INetwork<Float> {
    private final Vector3 startingPos;
    public Map<Vector3, EnumFacing> connections = new HashMap<Vector3, EnumFacing>();
    public final World world;

    public ElectricityNetwork(Vector3 start, World w) {
        this.startingPos = start;
        this.world = w;
    }

    @Override
    public Float produce(INetworkProducer producer, Vector3... ignore) {
        float stored = (float) producer.getStored();

        Set<Vector3> avaliableEnergyTiles = this.getAcceptors();

        if (!avaliableEnergyTiles.isEmpty()) {
            final float totalEnergyRequest = this.getRequest(ignore);

            if (totalEnergyRequest > 0) {
                for (Vector3 vec : avaliableEnergyTiles) {
                    TileEntity tileEntity = getWorld().getTileEntity(vec.toBlockPos());
                    Class<INetworkAcceptor<Float>> type = (Class<INetworkAcceptor<Float>>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                    if (type.isInstance(tileEntity) && !Arrays.asList(ignore).contains(vec)) {
                        INetworkAcceptor electricalTile = (INetworkAcceptor) tileEntity;
                        // TODO: Fix Direction
                        float energyToSend = stored * (float) electricalTile.getRequest(EnumFacing.NORTH) / totalEnergyRequest;

                        if (energyToSend > 0) {

                            // Calculate energy loss caused by resistance

                            // TODO: Fix unknown direction!
                            stored -= ((INetworkAcceptor<Float>) tileEntity).receive(EnumFacing.NORTH, energyToSend, true);
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
    public Float getRequest(Vector3... ignoreTiles) {
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
        float r = 0;
        for (float f : requests) {
            r += f;
        }

        return r;
    }

    /**
     * @return Returns all acceptors in this electricity network.
     */

    public Set<Vector3> getAcceptors() {
        return this.connections.keySet().stream().filter(a -> a instanceof INetworkAcceptor).collect(Collectors.toSet());
    }

    /**
     * @return Returns all producers in this electricity network.
     */

    public Set<Vector3> getProducers() {
        return this.connections.keySet().stream().filter(a -> a instanceof INetworkProducer).collect(Collectors.toSet());
    }

    /**
     * This function is called to refresh all in this network
     */
    @Override
    public void refresh() {
        refreshConnections();
        try {
            Iterator<Vector3> it = this.connections.keySet().iterator();

            while (it.hasNext()) {
                Vector3 vec = it.next();
                TileEntity conductor = world.getTileEntity(vec.toBlockPos());
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
            FMLLog.severe("Universal Electricity: Failed to refresh conductor.");
            e.printStackTrace();
        }
    }

    @Override
    public void refreshConnections() {
        this.connections.clear();
        TileEntity tile = world.getTileEntity(startingPos.toBlockPos());
        if (tile != null && !(tile.isInvalid())) {
            if (tile instanceof INetworkedTile) {
                this.connections = ((INetworkedTile) tile).getAdjacentConnections();
            } else if (tile instanceof IEnergyStorage) {
                this.connections = CreepinoUtils.searchForBlockOnSidesRecursive(tile, EnumFacing.values(), INetworkedTile.class, IEnergyStorage.class);
            }

        }
    }

    @Override
    public void merge(INetwork<Float> network) {
        if (network != null && network != this) {
            ElectricityNetwork newNetwork = new ElectricityNetwork(startingPos, this.getWorld());
            newNetwork.connections.putAll(this.connections);
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
            Set<Vector3> connectedBlocks = splitPoint.getAdjacentConnections().keySet();

            for (Vector3 v : connectedBlocks) {
                TileEntity connectedBlockA = world.getTileEntity(v.toBlockPos());

                if (connectedBlockA instanceof INetworkedTile) {
                    /**
                     * The connections A and B are not connected anymore. Give both of
                     * them a new network.
                     */
                    INetwork newNetwork = new ElectricityNetwork(startingPos, world);
                    TileEntity nodeTile = v.getTileEntity(((TileEntity) splitPoint).getWorld());

                    if (nodeTile instanceof INetworkedTile) {
                        if (nodeTile != splitPoint) {
                            newNetwork.getConnections().put(connectedBlockA, EnumFacing.NORTH);
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
    public Map<Vector3, EnumFacing> getConnections() {
        return this.connections;
    }

    @Override
    public World getWorld() {
        return world;
    }

}