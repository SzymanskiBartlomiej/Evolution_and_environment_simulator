package com.project;

public class World {
    public static void main(String[] args) {
        IMapEdge edges = new GlobeMapEdge(new Vector2d(10, 10));
        IWorldMap map = new ForestedEquatorsWorldMap(edges, new Vector2d(1, 7), new Vector2d(9, 9), 1, 2, 1, new SlightCorrectionMutation(2, 4), 4, 10, 5, new BitOfMadnessGenome());

        Vector2d animal1Position = new Vector2d(2, 2);
        Animal animal1 = new Animal(animal1Position, new int[]{2, 3, 5, 6, 3}, 10);

        Vector2d animal2Position = new Vector2d(4, 8);
        Animal animal2 = new Animal(animal2Position, new int[]{2, 3, 5, 1, 3}, 10);


        Animal[] animals = {animal1, animal2};
        IEngine engine = new SimulationEngine(map, animals, 15);
        System.out.println(map.toString());
        engine.run();


    }
}
