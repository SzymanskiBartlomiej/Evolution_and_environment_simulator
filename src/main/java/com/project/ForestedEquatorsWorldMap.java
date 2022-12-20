package com.project;

import java.util.*;

public class ForestedEquatorsWorldMap implements IWorldMap {
    private final IMutation mutation; // typ mutacji
    private final int grassEnergy; //energia z 1 trawy
    private final int energyLostPerDay; // dzienna utrata energii
    private final int grassPerDay; //ilośc trawy która rośnie co dziennie
    private final int energyToCopulate;
    private final int energyLostToCopulate;
    private final int startingEnergy; //startowa energia
    private final int genomeLength; //długość genomu
    private final IGenome genome;
    private final IMapEdge mapEdge;
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final MultiMap<Vector2d, Animal> animals = new MultiMap<>(new AnimalComparator());
    private final Vector2d equatorLowerLeft; // wystarczy może equatorWidth bo i tak wiemy ze ma być na srodku mapy i przez cało jej długość
    private final Vector2d equatorUpperRight;
    private int emptyEquatorFields;

    private int emptyNotEquatorFields;

    public ForestedEquatorsWorldMap(
            IMapEdge mapEdge, IGenome genome,IMutation mutation, Vector2d equatorLowerLeft,
            Vector2d equatorUpperRight,  int grassEnergy, int grassPerDay, int energyLostPerDay,
            int energyToCopulate, int energyLostToCopulate, int startingEnergy, int genomeLength,int startingGrassNum) {
        this.mutation = mutation;
        this.mapEdge = mapEdge;
        this.equatorLowerLeft = equatorLowerLeft;
        this.equatorUpperRight = equatorUpperRight;
        this.grassEnergy = grassEnergy;
        this.grassPerDay = grassPerDay;
        this.energyToCopulate = energyToCopulate;
        this.energyLostToCopulate = energyLostToCopulate;
        this.startingEnergy = startingEnergy;
        this.genomeLength = genomeLength;
        this.genome = genome;
        this.emptyEquatorFields = (equatorUpperRight.x - equatorLowerLeft.x) * (equatorUpperRight.y - equatorLowerLeft.y);
        this.emptyNotEquatorFields = (mapEdge.getUpperRight().x * mapEdge.getUpperRight().y) - emptyEquatorFields;
        this.energyLostPerDay = energyLostPerDay;
        Random rand = new Random();
        for (int i = 0; i < startingGrassNum; i++) {
            Vector2d grassPosition;
            // losowanie trawy na tym równiku nie ma sensu bo jak zostało tylko 1 pole to trwa to w nieskończoność??
            if (emptyEquatorFields > 0 && rand.nextInt(10) + 1 > 2) {
                do {
                    int x = rand.nextInt(equatorUpperRight.x - equatorLowerLeft.x) + equatorLowerLeft.x;
                    int y = rand.nextInt(equatorUpperRight.y - equatorLowerLeft.y) + equatorLowerLeft.y;
                    grassPosition = new Vector2d(x, y);
                } while (grasses.get(grassPosition) != null);
                this.emptyEquatorFields -= 1;
            } else if (emptyNotEquatorFields > 0) {
                do {
                    int x = rand.nextInt(mapEdge.getUpperRight().x - mapEdge.getLowerLeft().x + 1) + mapEdge.getLowerLeft().x;
                    int y = rand.nextInt(mapEdge.getUpperRight().y - mapEdge.getLowerLeft().y + 1) + mapEdge.getLowerLeft().y;
                    grassPosition = new Vector2d(x, y);
                } while (grasses.get(grassPosition) != null);
                this.emptyNotEquatorFields -= 1;
            } else return;
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    public void populate(int numOfAnimals) {
        Random rand = new Random();
        for (int i = 0; i < numOfAnimals; i++) {
            int x = rand.nextInt(mapEdge.getUpperRight().x - mapEdge.getLowerLeft().x + 1) + mapEdge.getLowerLeft().x;
            int y = rand.nextInt(mapEdge.getUpperRight().y - mapEdge.getLowerLeft().y + 1) + mapEdge.getLowerLeft().y;
            Vector2d animalPosition = new Vector2d(x, y);
            int[] genes = new int[genomeLength];
            for (int j = 0; j < genomeLength; j++) {
                genes[j] = rand.nextInt(8);
            }
            Animal animal = new Animal(animalPosition, genes, startingEnergy);
            animals.put(animalPosition, animal);
        }
    }

    @Override
    public void growGrass() {
        Random rand = new Random();
        for (int i = 0; i < grassPerDay; i++) {
            Vector2d grassPosition;
            // losowanie trawy na tym równiku nie ma sensu bo jak zostało tylko 1 pole to trwa to w nieskończoność??
            if (emptyEquatorFields > 0 && rand.nextInt(10) + 1 > 2) {
                do {
                    int x = rand.nextInt(equatorUpperRight.x - equatorLowerLeft.x) + equatorLowerLeft.x;
                    int y = rand.nextInt(equatorUpperRight.y - equatorLowerLeft.y) + equatorLowerLeft.y;
                    grassPosition = new Vector2d(x, y);
                } while (grasses.get(grassPosition) != null);
                this.emptyEquatorFields -= 1;
            } else if (emptyNotEquatorFields > 0) {
                do {
                    int x = rand.nextInt(mapEdge.getUpperRight().x - mapEdge.getLowerLeft().x + 1) + mapEdge.getLowerLeft().x;
                    int y = rand.nextInt(mapEdge.getUpperRight().y - mapEdge.getLowerLeft().y + 1) + mapEdge.getLowerLeft().y;
                    grassPosition = new Vector2d(x, y);
                } while (grasses.get(grassPosition) != null);
                this.emptyNotEquatorFields -= 1;
            } else return;
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    @Override
    public void moveAnimals() {
        /// TODO: observer do animals (multimap)

        for (Animal animal : animals.values()) {
            animals.remove(animal.getPosition(), animal);
            animal.energy -= energyLostPerDay;
            animal.age += 1;
            animal.moveUsingGene();
            animal.setCurrentGene(genome.nextGene(animal.getCurrentGene(), genomeLength));
            mapEdge.crossedEdge(animal);
            animals.put(animal.getPosition(), animal);
        }
    }

    @Override
    public void removeDeadAnimals() {
        for (Animal animal : animals.values()) {
            if (animal.energy <= 0) {
                animals.remove(animal.getPosition(), animal);
            }
        }
    }

    @Override
    public void eatGrass() {
        List<Vector2d> vector2dsToDelete = new ArrayList<>(); //musi tak być bo w trakcie iteracji nie moża usuwać kluczy
        for (Vector2d vector2d : this.grasses.keySet()) {
            if (animals.get(vector2d) != null) {
                animals.getHighest(vector2d).energy += grassEnergy;
                vector2dsToDelete.add(vector2d);
            }
        }
        for (Vector2d vector2d : vector2dsToDelete) {
            grasses.remove(vector2d);
        }
    }

    @Override
    public void copulation() {
        for (Vector2d vector2d : animals.keySet()) {
            if (animals.get(vector2d) != null && animals.get(vector2d).size() >= 2) {
                ArrayList<Animal> toCopulate = animals.get2Highest(vector2d);
                Animal a = toCopulate.get(0);
                Animal b = toCopulate.get(1);
                if (a.energy >= energyToCopulate && b.energy >= energyToCopulate) {
                    Random rand = new Random();
                    int genesFromStonger = Math.round((float) (a.energy / (a.energy + b.energy)) * genomeLength);
                    int[] genes = new int[genomeLength];
                    if (rand.nextInt(2) == 0) {
                        for (int i = 0; i < genomeLength; i++) {
                            if (i < genesFromStonger) genes[i] = a.getGenes()[i];
                            else genes[i] = b.getGenes()[i];
                        }
                    } else {
                        genesFromStonger = genomeLength - genesFromStonger;
                        for (int i = 0; i < genomeLength; i++) {
                            if (i > genesFromStonger) genes[i] = a.getGenes()[i];
                            else genes[i] = b.getGenes()[i];
                        }
                    }
                    a.energy -= energyLostToCopulate;
                    b.energy -= energyLostToCopulate;
                    a.numOfChildren += 1;
                    b.numOfChildren += 1;
                    genes = mutation.mutate(genes);
                    Animal animal = new Animal(vector2d, genes, 2 * energyLostToCopulate);
                    place(animal);
                }

            }
        }
    }

    @Override
    public void place(Animal animal) {
        Vector2d position = animal.getPosition();
        animals.put(position, animal);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    @Override
    public Object objectAt(Vector2d position) {
        if (animals.get(position) != null) {
            // zwraca ArrayListe
            return animals.get(position);
        }
        return grasses.get(position);
    }

    @Override
    public String toString() {
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(mapEdge.getLowerLeft(), mapEdge.getUpperRight());
    }

    public Collection<Animal> getAnimals() {
        // funkcja do testów
        return animals.values();
    }
}
