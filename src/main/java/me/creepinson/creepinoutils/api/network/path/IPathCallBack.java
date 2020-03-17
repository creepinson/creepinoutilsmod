package me.creepinson.creepinoutils.api.network.path;

import me.creepinson.creepinoutils.api.util.math.ForgeVector;

import java.util.Set;

public interface IPathCallBack {
    /**
     * @param finder      - The Pathfinder object.
     * @param currentNode - The node being iterated through.
     * @return A set of nodes connected to the currentNode. Essentially one should return a set of
     * neighboring nodes.
     */
    public Set<ForgeVector> getConnectedNodes(Pathfinder finder, ForgeVector currentNode);

    /**
     * Called when looping through nodes.
     *
     * @param finder - The Pathfinder.
     * @param node   - The node being searched.
     * @return True to stop the path finding operation.
     */
    public boolean onSearch(Pathfinder finder, ForgeVector node);
}