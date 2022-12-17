package com.project;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class SlightCorrectionMutation implements IMutation {
    private final int minMutationNum;
    private final int maxMutationNum;

    public SlightCorrectionMutation(int minMutationNum, int maxMutationNum) {
        this.minMutationNum = minMutationNum;
        this.maxMutationNum = maxMutationNum;
    }

    @Override
    public int[] mutate(int[] genes) {
        Random rand = new Random();
        int numOfGenes = rand.nextInt(maxMutationNum + 1 - minMutationNum) + minMutationNum;
        Integer[] arr = new Integer[genes.length];
        Arrays.setAll(arr, i -> i);
        Collections.shuffle(Arrays.asList(arr));
        for (int i = 0; i < numOfGenes; i++) {
            if (rand.nextInt(2) == 0)
                genes[arr[i]] = (genes[arr[i]] + 7) % 8; //zmiana -1 na 7
            else {
                genes[arr[i]] = (genes[arr[i]] + 1) % 8;
            }
        }
        return genes;
    }
}
