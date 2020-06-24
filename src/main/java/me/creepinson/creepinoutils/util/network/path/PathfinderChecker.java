package me.creepinson.creepinoutils.util.network.path;

import dev.throwouterror.util.math.Facing;
import dev.throwouterror.util.math.Tensor;
import me.creepinson.creepinoutils.base.BaseTile;
import me.creepinson.creepinoutils.util.TensorUtils;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

/**
 * Check if a conductor connects with another.
 *
 * @author Calclavia
 */
public class PathfinderChecker extends Pathfinder {
    public PathfinderChecker(final World world, final Class<?>[] targets, final BaseTile... ignoreConnector) {
        super(new IPathCallBack() {
            @Override
            public Set<Tensor> getConnectedNodes(Pathfinder finder, Tensor currentNode) {
                Set<Tensor> neighbors = new HashSet<>();

                for (int i = 0; i < 6; i++) {
                    Facing direction = Facing.byIndex(i);
                    Tensor position = currentNode.clone().offset(direction);
                    neighbors.add(position);
                }

                return neighbors;
            }

            @Override
            public boolean onSearch(Pathfinder finder, Tensor node) {
                for (Class<?> c : targets) {
                    if (c.isInstance(TensorUtils.getTile(world, node))) {
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