package com.project;

import java.util.Random;

public class BitOfMadnessGenome implements IGenome{
    private final int[] Genes;
    private int currentGene = 0;

    public BitOfMadnessGenome(int[] genes) {
        this.Genes = genes;
    }
    public int getCurrentGene(){
        return Genes[currentGene];
    }
    public void nextGene(){
        Random rand = new Random();
        int randGene;
        if(rand.nextInt(10)+1 > 2){
            currentGene = (currentGene+1)% (Genes.length);
        }
        else {
            do {
                randGene = rand.nextInt(Genes.length);
            }while (currentGene!=randGene);
        }
    }
}
