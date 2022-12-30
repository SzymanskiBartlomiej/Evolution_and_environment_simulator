package com.project;

public class Animal implements IMapElement, Comparable<Animal> {
    private MapDirection mapDirection;
    private Vector2d vector2d;
    int numOfChildren = 0;
    public int energy;
    int dayOfDeath = 0;
    int age = 0;
    private final int[] genes;
    private int currentGene = 0;
    private String texturePath = "src/main/resources/animal.png";

    public Animal(Vector2d vector2d, int[] genes, int startingEnergy) {
        this.vector2d = vector2d;
        this.mapDirection = MapDirection.N;
        this.genes = genes;
        this.energy = startingEnergy;
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

    public int getNumOfChildren() {
        return numOfChildren;
    }

    public int getDayOfDeath() {
        return dayOfDeath;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "o";
    }

    @Override
    public String getTexturePath() {
        return texturePath;
    }

    @Override
    public int compareTo(Animal other) {
        return 0;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }
}
