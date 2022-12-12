package com.project;

public class BitOfMadnessGenome implements IGenome{
    private final int[] Genes;
    private int currentGene = 0;

    public BitOfMadnessGenome(int[] genes) {
        this.Genes = genes;
    }
    public int getCurrentGene(){
        return Genes[currentGene];
    }
    // TODO: w 80% przypadków zwierzak po wykonaniu
    //    // genu aktywuje gen następujący zaraz po nim,
    //    // w 20% przypadków przeskakuje jednak do innego, losowego genu.`
    public void nextGene(){
        currentGene = (currentGene+1)% (Genes.length);
    }
}
