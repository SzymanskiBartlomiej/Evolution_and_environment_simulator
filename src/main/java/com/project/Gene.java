package com.project;

public class Gene {
    private int[] Genes;
    private int currentGene = 0;

    public Gene(int[] genes) {
        this.Genes = genes;
    }
    public int getCurrentGene(){
        return Genes[currentGene];
    }
    public void nextGene(){
        currentGene = (currentGene+1)% (Genes.length);
    }
}
