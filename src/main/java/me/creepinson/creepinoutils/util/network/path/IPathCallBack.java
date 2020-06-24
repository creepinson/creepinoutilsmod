package me.creepinson.creepinoutils.util.network.path;

import dev.throwouterror.util.math.Tensor;

import java.util.Set;

public interface IPathCallBack {
    /**
     * @param finder      - The Pathfinder object.
     * @param currentNode - The node being iterated through.
     * @return A set of nodes connected to the currentNode. Essentially one should return a set of
     * neighboring nodes.
     */
    Set<Tensor> getConnectedNodes(Pathfinder finder, Tensor currentNode);

    /**
     * Called when looping through nodes.
     *
     * @param finder - The Pathfinder.
     * @param node   - The node being searched.
     * @return True to stop the path finding operation.
     */
    boolean onSearch(Pathfinder finder, Tensor node);
}