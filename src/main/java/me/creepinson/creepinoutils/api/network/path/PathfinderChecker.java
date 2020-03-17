package me.creepinson.creepinoutils.api.network.path;

import me.creepinson.creepinoutils.api.network.INetworkTile;
import me.creepinson.creepinoutils.api.util.math.Facing;
import me.creepinson.creepinoutils.api.util.math.ForgeVector;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;


/**
 * Check if a conductor connects with another.
 *
 * @author Calclavia
 */
public class PathfinderChecker extends Pathfinder {
    public PathfinderChecker(final World world, final Class[] targets, final INetworkTile... ignoreConnector) {
        super(new IPathCallBack() {
            @Override
            public Set<ForgeVector> getConnectedNodes(Pathfinder finder, ForgeVector currentNode) {
                Set<ForgeVector> neighbors = new HashSet<>();

                for (int i = 0; i < 6; i++) {
                    Facing direction = Facing.byIndex(i);
                    ForgeVector position = currentNode.clone().modifyPositionFromSide(direction);
                    TileEntity connectedBlock = position.getTileEntity(world);

/*					if (connectedBlock instanceof INetworkedTile && !Arrays.asList(ignoreConnector).contains(connectedBlock))
					{
						if (((INetworkedTile) connectedBlock).canConnectTo(world, position, direction.getOpposite()))
						{

						}
					}*/
                    neighbors.add(position);
                }

                return neighbors;
            }

            @Override
            public boolean onSearch(Pathfinder finder, ForgeVector node) {
                for (Class c : targets) {
                    if (c.isInstance(node.getTileEntity(world))) {
                        finder.results.add(node);

                        return true;
                    }
                }
                System.out.println(node.toString());

                return false;
            }
        });
    }
}