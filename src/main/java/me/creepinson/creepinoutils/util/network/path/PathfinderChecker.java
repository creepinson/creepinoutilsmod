package me.creepinson.creepinoutils.util.network.path;

import me.creepinson.creepinoutils.api.util.math.Facing;
import me.creepinson.creepinoutils.api.util.math.Vector3;
import me.creepinson.creepinoutils.base.BaseTile;
import me.creepinson.creepinoutils.util.VectorUtils;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;


/**
 * Check if a conductor connects with another.
 *
 * @author Calclavia
 */
public class PathfinderChecker extends Pathfinder {
    public PathfinderChecker(final World world, final Class[] targets, final BaseTile... ignoreConnector) {
        super(new IPathCallBack() {
            @Override
            public Set<Vector3> getConnectedNodes(Pathfinder finder, Vector3 currentNode) {
                Set<Vector3> neighbors = new HashSet<>();

                for (int i = 0; i < 6; i++) {
                    Facing direction = Facing.byIndex(i);
                    Vector3 position = currentNode.clone().modifyPositionFromSide(direction);
                    neighbors.add(position);
                }

                return neighbors;
            }

            @Override
            public boolean onSearch(Pathfinder finder, Vector3 node) {
                for (Class c : targets) {
                    if (c.isInstance(VectorUtils.getTile(world, node))) {
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