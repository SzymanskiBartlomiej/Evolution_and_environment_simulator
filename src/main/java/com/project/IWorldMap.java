package com.project;

import java.util.Collection;

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
    Collection<Animal> getAnimals();
    Animal selectStrongestAnimal(Vector2d vector2d);
    Animal[] select2StrongestAnimal(Vector2d vector2d);
}
