package com.project;

public class Animal implements IMapElement,Comparable<Animal>{
    private MapDirection mapDirection;
    private Vector2d vector2d;
    private int age = 0;
    private int numOfChildren = 0;

    private IGenome genome;
    //TODO: konstruktor powinnien dostać IGenome genom a nie int[] geny
    public Animal(Vector2d vector2d,int[] genes) {
        this.vector2d = vector2d;
        this.mapDirection = MapDirection.N;
        this.genome = new BitOfMadnessGenome(genes);
    }
    public void move(){
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
        return mapDirection.toString();
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
