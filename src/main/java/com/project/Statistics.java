package com.project;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class Statistics {
    private final IWorldMap map;
    public int numOfAnimals;
    public int numOfGrasses;
    public int numOfEmptyFields;
    public int[] mostPopularGenes;
    public int averageAnimalEnergy;
    public int averageAnimalLifeSpan;

    public Statistics(IWorldMap map) {
        this.map = map;
        updateStats();
    }
    public void updateStats(){
        numOfAnimals = map.getAnimals().size();
        numOfGrasses = map.getGrasses().size();
        numOfEmptyFields = map.getEmptyFields();
        averageAnimalEnergy = getAverageEnergy();
        averageAnimalLifeSpan = map.averageAnimalLifeSpan();
        mostPopularGenes = getMostPopularGenes();
    }
    public int getAverageEnergy(){
        Collection<Animal> animals = this.map.getAnimals();
        if(animals.size()==0){return 0;}
        int averageEnergy = 0;
        for(Animal animal : animals){
            averageEnergy += animal.energy;
        }
        return averageEnergy/animals.size();
    }
    public int[] getMostPopularGenes(){
        int maxCount = 0;
        int[] res = {};
        HashMap<int[],Integer>  genomeCount = map.getGenomeCount();
        for (int[] gene : genomeCount.keySet()){
            if(maxCount<genomeCount.get(gene)){
                maxCount = genomeCount.get(gene);
                res = gene;
            }
        }
        return res;
    }
}
