package com.project;

import java.util.Random;

public class BitOfMadnessGenome implements IGenome {
    public int nextGene(int currentGene, int genomeLength) {
        Random rand = new Random();
        int randGene;
        if (rand.nextInt(10) + 1 > 2) {
            currentGene = (currentGene + 1) % (genomeLength);
            return currentGene;
        } else {
            do {
                randGene = rand.nextInt(genomeLength);
            } while (currentGene != randGene);
            return randGene;
        }
    }
}
