package com.project;

public class Animal implements IMapElement,Comparable<Animal>{
    private MapDirection mapDirection;
    private Vector2d vector2d;
    int numOfChildren = 0;
    int energy;
    int age = 0;
    private IGenome genome;
    public Animal(Vector2d vector2d,IGenome genes , int energy) {
        this.vector2d = vector2d;
        this.mapDirection = MapDirection.N;
        this.genome = genes;
        this.energy = energy;
    }


    public void move(Vector2d position){
        this.vector2d = position;
    }
    public MapDirection getMapDirection(){return this.mapDirection;}

    public void setMapDirection(MapDirection mapDirection) {
        this.mapDirection = mapDirection;
    }

    public void moveUsingGene(){
        int rotation = genome.getCurrentGene();
        for(int i = rotation;i>0;i--){
            this.mapDirection = mapDirection.next();
        }
        this.vector2d = mapDirection.toUnitVector().add(vector2d);
        genome.nextGene();
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
