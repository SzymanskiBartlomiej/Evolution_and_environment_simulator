package com.project;

public class World {
    public static void main(String[] args) {
        int moves[] = {0,0,7,0,4};
        Animal animal = new Animal(new Vector2d(2,2),moves);
        System.out.println(animal.getPosition().toString() + " "+ animal);
        for (int i=0; i<moves.length ; i++){
            animal.move();
            System.out.println(animal.getPosition().toString() + " "+ animal + " " + moves[i]);
        }
    }
}
