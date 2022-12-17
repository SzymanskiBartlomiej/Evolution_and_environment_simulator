package com.project;

import java.util.Comparator;

public class AnimalComparator implements Comparator<Animal> {
    @Override
    public int compare(Animal a, Animal b) {
        if (a.energy != b.energy) {
            return a.energy - b.energy;
        }
        if (a.age != b.age) {
            return a.age - b.age;
        }
        if (a.numOfChildren != b.numOfChildren) {
            return a.numOfChildren - b.numOfChildren;
        }
        // bo 2 animale są równe jak mają ten sam hashcode
        return a.hashCode() - b.hashCode();
    }
}

