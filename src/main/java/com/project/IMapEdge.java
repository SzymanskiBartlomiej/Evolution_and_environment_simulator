package com.project;

/**
 * The interface responsible for handling map edges
 */
public interface IMapEdge {
    /**
     * Indicate what happens to an animal which crosses the map edge.
     *
     * @param animal The animal checked for the crossing of an edge
     *
     */
    void crossedEdge(Animal animal);

    Vector2d getLowerLeft();

    Vector2d getUpperRight();
}
