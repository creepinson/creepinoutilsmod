package me.creepinson.mod.api.network;

import java.util.*;
import java.util.stream.Collectors;

import com.creativemd.creativecore.common.utils.math.vec.Vec;
import com.creativemd.creativecore.common.utils.type.Pair;
import me.creepinson.mod.api.INetworkedTile;
import me.creepinson.mod.api.util.BlockTileContainer;
import me.creepinson.mod.api.util.math.Vector3;
import mekanism.common.transmitters.grid.EnergyNetwork;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.FMLLog;

public abstract class ElectricityNetwork implements INetwork<Float> {
    public Map<Vector3, EnumFacing> connections = new HashMap<Vector3, EnumFacing>();
    public final World world;
    public ElectricityNetwork(World w) {
        this.world = w;
    }
    @Override
    public Float produce(INetworkedTile producer, Vector3... ignore) {
        float stored = (float) producer.getStored();

        Set<Vector3> avaliableEnergyTiles = this.getAcceptors();

        if (!avaliableEnergyTiles.isEmpty()) {
            final float totalEnergyRequest = this.getRequest(ignore);

            if (totalEnergyRequest > 0) {
                for (Vector3 vec : avaliableEnergyTiles) {
                    TileEntity tileEntity = getWorld().getTileEntity(vec.toBlockPos());
                    if (tileEntity instanceof INetworkAcceptor && !Arrays.asList(ignore).contains(tileEntity)) {
                        INetworkAcceptor electricalTile = (INetworkAcceptor) tileEntity;
                        // TODO: Fix Direction
                        float energyToSend = stored * (float)electricalTile.getRequest(EnumFacing.NORTH) / totalEnergyRequest;

                        if (energyToSend > 0) {

                            // Calculate energy loss caused by resistance

                            // TODO: Fix unknown direction!
                            stored -= ((IElectrical) tileEntity).receiveElectricity(ForgeDirection.UNKNOWN, electricityToSend, true);
                        }
                    }
                }
            }
        }
        return energy;
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
            }
            it.remove();
        }

//        ElectricityRequestEvent evt = new ElectricityRequestEvent(mergedPack, ignoreTiles);
//        MinecraftForge.EVENT_BUS.post(evt);
        return merged;
    }

    /**
     * @return Returns all producers in this electricity network.
     */

    public Set<Vector3> getAcceptors() {
        return this.connections.keySet().stream().filter(a -> a instanceof INetworkAcceptor).collect(Collectors.toSet());
    }

    /**
     * This function is called to refresh all conductors in this network
     */
    @Override
    public void refresh() {
        this.electricalTiles.clear();

        try {
            Iterator<IConductor> it = this.conductors.iterator();

            while (it.hasNext()) {
                IConductor conductor = it.next();

                if (conductor == null) {
                    it.remove();
                } else if (((TileEntity) conductor).isInvalid()) {
                    it.remove();
                } else {
                    conductor.setNetwork(this);
                }

                for (TileEntity acceptor : conductor.getAdjacentConnections()) {
                    if (!(acceptor instanceof IConductor)) {
                        this.electricalTiles.add(acceptor);
                    }
                }
            }
        } catch (Exception e) {
            FMLLog.severe("Universal Electricity: Failed to refresh conductor.");
            e.printStackTrace();
        }
    }

    @Override
    public void merge(ElectricityNetwork network) {
        if (network != null && network != this) {
            ElectricityNetwork newNetwork = new ElectricityNetwork() {
                @Override
                public World getWorld() {
                    return null;
                }
            };
            newNetwork.connections.putAll(this.connections);
            newNetwork.refresh();
        }
    }

    @Override
    public void split(IConductor splitPoint) {
        if (splitPoint instanceof TileEntity) {
            this.getConductors().remove(splitPoint);

            /**
             * Loop through the connected blocks and attempt to see if there are connections between
             * the two points elsewhere.
             */
            TileEntity[] connectedBlocks = splitPoint.getAdjacentConnections();

            for (int i = 0; i < connectedBlocks.length; i++) {
                TileEntity connectedBlockA = connectedBlocks[i];

                if (connectedBlockA instanceof INetworkConnection) {
                    for (int ii = 0; ii < connectedBlocks.length; ii++) {
                        final TileEntity connectedBlockB = connectedBlocks[ii];

                        if (connectedBlockA != connectedBlockB && connectedBlockB instanceof INetworkConnection) {
                            Pathfinder finder = new PathfinderChecker(((TileEntity) splitPoint).worldObj, (INetworkConnection) connectedBlockB, splitPoint);
                            finder.init(new Vector3(connectedBlockA));

                            if (finder.results.size() > 0) {
                                /**
                                 * The connections A and B are still intact elsewhere. Set all
                                 * references of wire connection into one network.
                                 */

                                for (Vector3 node : finder.closedSet) {
                                    TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).worldObj);

                                    if (nodeTile instanceof INetworkProvider) {
                                        if (nodeTile != splitPoint) {
                                            ((INetworkProvider) nodeTile).setNetwork(this);
                                        }
                                    }
                                }
                            } else {
                                /**
                                 * The connections A and B are not connected anymore. Give both of
                                 * them a new network.
                                 */
                                IElectricityNetwork newNetwork = new ElectricityNetwork();

                                for (Vector3 node : finder.closedSet) {
                                    TileEntity nodeTile = node.getTileEntity(((TileEntity) splitPoint).worldObj);

                                    if (nodeTile instanceof INetworkProvider) {
                                        if (nodeTile != splitPoint) {
                                            newNetwork.getConductors().add((IConductor) nodeTile);
                                        }
                                    }
                                }

                                newNetwork.refresh();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "ElectricityNetwork[" + this.hashCode() + "|Wires:" + this.conductors.size() + "|Acceptors:" + this.electricalTiles.size() + "]";
    }

    @Override
    public HashMap<Vector3, EnumFacing> getConnections() {
        return this.connections;
    }

    @Override
    public Object produce(INetworkedTile producer, Vector3... ignore) {
        return null;
    }
}}