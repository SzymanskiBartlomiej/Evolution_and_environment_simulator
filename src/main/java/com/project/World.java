package com.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {
    public static void main(String[] args) {
        IMapEdge edges = new GlobeMapEdge(new Vector2d(10,10));
        IWorldMap map = new ForestedEquatorsWorldMap(edges,new Vector2d(1,7),new Vector2d(9,9),1,2,1);

        Vector2d animal1Position = new Vector2d(2,2);
        IGenome genesAnimal1 = new BitOfMadnessGenome(new int[] {2,3,5,6,3});
        Animal animal1 = new Animal(animal1Position,genesAnimal1,10);

        Vector2d animal2Position = new Vector2d(4,8);
        IGenome genesAnimal2 = new BitOfMadnessGenome(new int[] {2,3,5,1,3});
        Animal animal2 = new Animal(animal2Position,genesAnimal2,10);


        Animal[] animals = {animal1,animal2};
        IEngine engine = new SimulationEngine(map,animals,15);
        System.out.println(map.toString());
        engine.run();



}}
