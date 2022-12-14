package com.project;
/**
 * The interface responsible for handling map edges
 */
public interface IMapEdge {
    /**
     * Indicate what happens to an animal which crosses the map edge.
     *
     * @param position
     *            The position checked for the crossing of an edge
     * @return Vector2d position where animal should move to.
     */
    void crossedEdge(Animal animal);
}
