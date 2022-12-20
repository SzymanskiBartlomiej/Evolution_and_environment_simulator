package com.project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class ForestedEquatorsWorldMapTest {
    @Test
    public void moveAnimalsTest(){
        System.out.println("Move Animals Test");
        IMapEdge edges = new GlobeMapEdge(new Vector2d(20, 20));
        IWorldMap map = new ForestedEquatorsWorldMap(edges,new BitOfMadnessGenome(),new SlightCorrectionMutation(2, 4), new Vector2d(1, 7), new Vector2d(9, 9), 5, 5, 1 , 5, 4, 10, 5,30 );

        Vector2d animal1Position = new Vector2d(2,2);
        Animal animal1 = new Animal(animal1Position,new int[] {1},10);
        Animal[] animals = {animal1};

        IEngine engine = new SimulationEngine(map,animals,1);
        System.out.println(map);
        engine.run();
        assertEquals(animal1.getPosition(),new Vector2d(3,3));
        assertEquals(animal1.getMapDirection(),MapDirection.NE);
    }
    @Test
    public void removeDeadAnimalsTest(){
        System.out.println("remove Dead Animals Test");

        IMapEdge edges = new GlobeMapEdge(new Vector2d(20, 20));
        IWorldMap map = new ForestedEquatorsWorldMap(edges,new BitOfMadnessGenome(),new SlightCorrectionMutation(2, 4), new Vector2d(1, 7), new Vector2d(9, 9), 5, 5, 1 , 5, 4, 1, 5,30 );

        Vector2d animal1Position = new Vector2d(2,2);
        // animal ma starting energy 0
        Animal animal1 = new Animal(animal1Position,new int[] {1},0);
        Animal[] animals = {animal1};

        IEngine engine = new SimulationEngine(map,animals,1);
        System.out.println(map);
        engine.run();
        assertTrue(map.getAnimals().isEmpty());
    }
    @Test
    public void copulation(){
        System.out.println("copulation test");
        IMapEdge edges = new GlobeMapEdge(new Vector2d(20, 20));
        IWorldMap map = new ForestedEquatorsWorldMap(edges,new BitOfMadnessGenome(),new SlightCorrectionMutation(2, 4), new Vector2d(1, 7), new Vector2d(9, 9), 5, 5, 1 , 5, 4, 10, 5,30 );

        Vector2d animal1Position = new Vector2d(2,2);
        Animal animal1 = new Animal(animal1Position,new int[] {0,0,0,0,0},10);

        Vector2d animal2Position = new Vector2d(2,2);
        Animal animal2 = new Animal(animal1Position,new int[] {0,0,0,0,0},10);


        Animal[] animals = {animal1,animal2};

        IEngine engine = new SimulationEngine(map,animals,1);

        System.out.println(map);
        engine.run();
        System.out.println(map.getAnimals());

        assertEquals(map.getAnimals().size(),3);

        // bo startingEnergy - energyLostPerDay - energyToCopulate = 5
        for(Animal animal : map.getAnimals()){
            if(animal == animal1 || animal==animal2){assertEquals(animal.energy,5);;}
            else{assertEquals(animal.energy,8);}
        }
    }
}
