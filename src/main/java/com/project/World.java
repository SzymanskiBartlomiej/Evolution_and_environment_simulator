package com.project;

public class World {
    public static void main(String[] args) {
        IMapEdge edge = new GlobeMapEdge(new Vector2d(0,0),new Vector2d(2,2));
        System.out.println(edge.crossedEdge(new Vector2d(3,-1)).toString()); //teścik na działanie edga
        int[] moves = {0,0,7,0,4};
        Animal animal = new Animal(new Vector2d(2,2),moves);
        System.out.println(animal.getPosition().toString() + " "+ animal);
        for (int move : moves) {
            animal.move();
            System.out.println(animal.getPosition().toString() + " " + animal + " " + move);
        }
    }
}
