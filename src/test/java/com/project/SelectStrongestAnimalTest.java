package com.project;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SelectStrongestAnimalTest {

    @Test
    public void SelectStrongestTest(){
        IMapEdge edges = new GlobeMapEdge(new Vector2d(10,10));
        IWorldMap map = new ForestedEquatorsWorldMap(edges,new Vector2d(1,7),new Vector2d(9,9),1,2,1);

        Vector2d animal1Position = new Vector2d(2,2);
        IGenome genesAnimal1 = new BitOfMadnessGenome(new int[] {0});
        Animal animal1 = new Animal(animal1Position,genesAnimal1,5);

        Vector2d animal2Position = new Vector2d(2,2);
        IGenome genesAnimal2 = new BitOfMadnessGenome(new int[] {0});
        Animal animal2 = new Animal(animal2Position,genesAnimal2,10);

        Animal[] animals = {animal1,animal2};
        IEngine engine = new SimulationEngine(map,animals,0);
        System.out.println(map);
        assertEquals(map.selectStrongestAnimal(animal2Position),animal2);
    }

    @Test
    public void Select2StrongestTest(){
        IMapEdge edges = new GlobeMapEdge(new Vector2d(10,10));
        IWorldMap map = new ForestedEquatorsWorldMap(edges,new Vector2d(1,7),new Vector2d(9,9),1,2,1);

        Vector2d animal1Position = new Vector2d(2,2);
        IGenome genesAnimal1 = new BitOfMadnessGenome(new int[] {0});
        Animal animal1 = new Animal(animal1Position,genesAnimal1,5);

        Vector2d animal2Position = new Vector2d(2,2);
        IGenome genesAnimal2 = new BitOfMadnessGenome(new int[] {0});
        Animal animal2 = new Animal(animal2Position,genesAnimal2,10);

        Vector2d animal3Position = new Vector2d(2,2);
        IGenome genesAnimal3 = new BitOfMadnessGenome(new int[] {0});
        Animal animal3 = new Animal(animal3Position,genesAnimal3,8);

        Animal[] animals = {animal1,animal2,animal3};
        IEngine engine = new SimulationEngine(map,animals,0);
        System.out.println(map);
        Animal[] result =  {animal3,animal2};
        assertNotEquals(map.select2StrongestAnimal(animal2Position)[0],animal1);
        assertNotEquals(map.select2StrongestAnimal(animal2Position)[1],animal1);
    }
}
