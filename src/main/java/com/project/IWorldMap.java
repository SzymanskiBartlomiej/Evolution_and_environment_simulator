package com.project;


import java.util.Collection;

public interface IWorldMap {
    void populate(int numOfAnimals);
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
    Collection<Animal> getAnimals();
}
