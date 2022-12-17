package com.project;

public interface IWorldMap {
    /**
     * Grows grass on the map.
     */
    void growGrass();
    void moveAnimals();
    void removeDeadAnimals();
    void eatGrass();
    void copulation();
    void place(Animal animal);
    boolean isOccupied(Vector2d position);
    Object objectAt(Vector2d position);
}
