package com.project;


public class World {
    public static void main(String[] args) {
        IMapEdge edges = new GlobeMapEdge(new Vector2d(20, 20));
        IWorldMap map = new ForestedEquatorsWorldMap(edges,new BitOfMadnessGenome(),new SlightCorrectionMutation(2, 4), new Vector2d(1, 7), new Vector2d(9, 9), 5, 5, 1 , 5, 4, 10, 5,30 );
//
//        Vector2d animal1Position = new Vector2d(2, 2);
//        Animal animal1 = new Animal(animal1Position, new int[]{2, 3, 5, 6, 3}, 10);
//
//        Vector2d animal2Position = new Vector2d(4, 8);
//        Animal animal2 = new Animal(animal2Position, new int[]{2, 3, 5, 1, 3}, 10);
//
//        Vector2d animal3Position = new Vector2d(1, 1);
//        Animal animal3 = new Animal(animal3Position, new int[]{0, 4, 3, 5, 6}, 10);
//
//        Vector2d animal4Position = new Vector2d(1, 3);
//        Animal animal4 = new Animal(animal4Position, new int[]{4, 7, 5, 6, 2}, 10);
//
//        Animal[] animals = {animal1, animal2,animal3,animal4};
//        IEngine engine = new SimulationEngine(map, animals, 60);
//        engine.run();
        map.populate(15);
        IEngine engine = new SimulationEngine(map, new Animal[]{}, 60);
        engine.run();

    }
}
