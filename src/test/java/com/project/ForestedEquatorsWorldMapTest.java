package com.project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class ForestedEquatorsWorldMapTest {
    @Test
    public void moveAnimalsTest(){
        IMapEdge edges = new GlobeMapEdge(new Vector2d(10,10));
        IWorldMap map = new ForestedEquatorsWorldMap(edges,new Vector2d(1,7),new Vector2d(9,9),1,2,1);

        Vector2d animal1Position = new Vector2d(2,2);
        IGenome genesAnimal1 = new BitOfMadnessGenome(new int[] {1});
        Animal animal1 = new Animal(animal1Position,genesAnimal1,10);
        Animal[] animals = {animal1};

        IEngine engine = new SimulationEngine(map,animals,1);
        System.out.println(map);
        engine.run();
        assertEquals(animal1.getPosition(),new Vector2d(3,3));
        assertEquals(animal1.getMapDirection(),MapDirection.NE);
    }
    @Test
    public void removeDeadAnimalsTest(){
        IMapEdge edges = new GlobeMapEdge(new Vector2d(10,10));
        IWorldMap map = new ForestedEquatorsWorldMap(edges,new Vector2d(1,7),new Vector2d(9,9),1,2,1);

        Vector2d animal1Position = new Vector2d(2,2);
        IGenome genesAnimal1 = new BitOfMadnessGenome(new int[] {1});
        Animal animal1 = new Animal(animal1Position,genesAnimal1,0);
        Animal[] animals = {animal1};

        IEngine engine = new SimulationEngine(map,animals,1);
        System.out.println(map);
        engine.run();
        System.out.println(map.getAnimals());
        assertTrue(map.getAnimals().isEmpty());
    }
}
