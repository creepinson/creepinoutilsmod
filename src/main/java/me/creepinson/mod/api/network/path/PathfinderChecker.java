package me.creepinson.mod.api.network.path;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import me.creepinson.mod.api.network.INetworkAcceptor;
import me.creepinson.mod.api.network.INetworkedTile;
import me.creepinson.mod.api.util.math.Vector3;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;


/**
 * Check if a conductor connects with another.
 * 
 * @author Calclavia
 * 
 */
public class PathfinderChecker extends Pathfinder
{
	public PathfinderChecker(final World world, final INetworkedTile targetConnector, final INetworkedTile... ignoreConnector)
	{
		super(new IPathCallBack()
		{
			@Override
			public Set<Vector3> getConnectedNodes(Pathfinder finder, Vector3 currentNode)
			{
				Set<Vector3> neighbors = new HashSet<Vector3>();

				for (int i = 0; i < 6; i++)
				{
					EnumFacing direction = EnumFacing.byIndex(i);
					Vector3 position = currentNode.clone().modifyPositionFromSide(direction);
					TileEntity connectedBlock = position.getTileEntity(world);

					if (connectedBlock instanceof INetworkAcceptor && !Arrays.asList(ignoreConnector).contains(connectedBlock))
					{
						if (((INetworkAcceptor) connectedBlock).canConnectTo(world, position, direction.getOpposite()))
						{
							neighbors.add(position);
						}
					}
				}

				return neighbors;
			}

			@Override
			public boolean onSearch(Pathfinder finder, Vector3 node)
			{
				if (node.getTileEntity(world) == targetConnector)
				{
					finder.results.add(node);
					return true;
				}

				return false;
			}
		});
	}
}