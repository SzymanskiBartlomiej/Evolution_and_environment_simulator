package com.project;

public class SimulationEngine implements IEngine {
    private final IWorldMap map;
    private final int days;

    public SimulationEngine(IWorldMap iWorldMap, Animal[] animals, int days) {
        this.map = iWorldMap;
        this.days = days;
        for (Animal animal : animals) {
            this.map.place(animal);
        }
    }

    @Override
    public void run() {
        for (int i = days; i > 0; i--) {
            System.out.println("day "+ (days - i));
            map.removeDeadAnimals();
            map.moveAnimals();
            map.eatGrass();
            map.copulation();
            map.growGrass();
            System.out.println(map.toString());
        }
    }
}
