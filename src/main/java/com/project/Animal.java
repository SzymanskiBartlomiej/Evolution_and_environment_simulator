package com.project;

public class Animal implements IMapElement, Comparable<Animal> {
    private MapDirection mapDirection;
    private Vector2d vector2d;
    int numOfChildren = 0;
    int energy;
    int age = 0;
    private final int[] genes;
    private int currentGene = 0;

    public Animal(Vector2d vector2d, int[] genes, int startingEnergy) {
        this.vector2d = vector2d;
        this.mapDirection = MapDirection.N;
        this.genes = genes;
        this.energy = energy;
    }

    public void move(Vector2d position) {
        this.vector2d = position;
    }

    public MapDirection getMapDirection() {
        return this.mapDirection;
    }

    public int[] getGenes() {
        return genes;
    }

    public int getCurrentGene() {
        return currentGene;
    }

    public void setCurrentGene(int currentGene) {
        this.currentGene = currentGene;
    }

    public void setMapDirection(MapDirection mapDirection) {
        this.mapDirection = mapDirection;
    }

    public void moveUsingGene() {
        int rotation = genes[currentGene];
        for (int i = rotation; i > 0; i--) {
            this.mapDirection = mapDirection.next();
        }
        this.vector2d = mapDirection.toUnitVector().add(vector2d);
    }

    public Vector2d getPosition() {
        return vector2d;
    }

    @Override
    public String toString() {
        return "o";
    }

    @Override
    public String getTexturePath() {
        return null;
    }

    @Override
    public int compareTo(Animal other) {
        return 0;
    }
}
