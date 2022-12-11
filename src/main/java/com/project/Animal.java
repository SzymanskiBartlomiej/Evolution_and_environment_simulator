package com.project;

public class Animal {
    private MapDirection mapDirection;
    private Vector2d vector2d;
    private Gene gene;

    public Animal(Vector2d vector2d,int[] genes) {
        this.vector2d = vector2d;
        this.mapDirection = MapDirection.N;
        this.gene = new Gene(genes);
    }
    public void move(){
        int rotation = gene.getCurrentGene();
        for(int i = rotation;i>0;i--){
            this.mapDirection = mapDirection.next();
        }
        this.vector2d = mapDirection.toUnitVector().add(vector2d);
        gene.nextGene();
    }

    public Vector2d getPosition() {
        return vector2d;
    }

    @Override
    public String toString() {
        return mapDirection.toString();
    }
}
