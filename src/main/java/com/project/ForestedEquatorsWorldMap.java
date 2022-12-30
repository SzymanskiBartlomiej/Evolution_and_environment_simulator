package com.project;

import java.util.*;
import java.util.List;

public class ForestedEquatorsWorldMap implements IWorldMap {
    private final IMutation mutation; // typ mutacji
    private final int grassEnergy; //energia z 1 trawy
    private final int energyLostPerDay; // dzienna utrata energii
    private final int grassPerDay; //ilośc trawy która rośnie co dziennie
    private final int energyToCopulate;
    private final int energyLostToCopulate;
    private final int startingEnergy; //startowa energia
    private final int genomeLength; //długość genomu
    private int emptyEquatorFields;
    private int emptyNotEquatorFields;
    private final IGenome genome;
    private final IMapEdge mapEdge;
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final MultiMap<Vector2d, Animal> animals = new MultiMap<>(new AnimalComparator());
    private final Vector2d equatorLowerLeft; // wystarczy może equatorWidth bo i tak wiemy ze ma być na srodku mapy i przez cało jej długość
    private final Vector2d equatorUpperRight;
    private int deadAnimals = 0;
    private int sumAnimalLifeSpan = 0;
    private final HashMap<int[],Integer> genomeCount = new HashMap<>();

    public ForestedEquatorsWorldMap(
            IMapEdge mapEdge, IGenome genome,IMutation mutation, Map<String, Integer> jsonConfiguration) {
        this.mutation = mutation;
        this.mapEdge = mapEdge;
        this.equatorLowerLeft = new Vector2d(0,jsonConfiguration.get("height")/2 - jsonConfiguration.get("equatorHeight")/2);
        this.equatorUpperRight = new Vector2d(jsonConfiguration.get("width")-1,jsonConfiguration.get("height")/2+jsonConfiguration.get("equatorHeight")/2);
        this.grassEnergy = jsonConfiguration.get("grassEnergy");
        this.grassPerDay = jsonConfiguration.get("grassPerDay");
        this.energyToCopulate = jsonConfiguration.get("energyToCopulate");
        this.energyLostToCopulate = jsonConfiguration.get("energyLostToCopulate");
        this.startingEnergy = jsonConfiguration.get("startingEnergy");
        this.genomeLength = jsonConfiguration.get("genomeLength");
        this.genome = genome;
        this.emptyEquatorFields = (equatorUpperRight.x - equatorLowerLeft.x+1) * (equatorUpperRight.y - equatorLowerLeft.y+1);
        this.emptyNotEquatorFields = ((mapEdge.getUpperRight().x+1) * (mapEdge.getUpperRight().y+1)) - emptyEquatorFields;
        this.energyLostPerDay = jsonConfiguration.get("energyLostPerDay");
        Random rand = new Random();
        for (int i = 0; i < jsonConfiguration.get("startingGrassNum"); i++) {
            Vector2d grassPosition;
            if (emptyEquatorFields > 0 && rand.nextInt(10) + 1 > 2) {
                do {
                    int x = rand.nextInt(equatorUpperRight.x - equatorLowerLeft.x+1) + equatorLowerLeft.x;
                    int y = rand.nextInt(equatorUpperRight.y - equatorLowerLeft.y+1) + equatorLowerLeft.y;
                    grassPosition = new Vector2d(x, y);
                } while (grasses.get(grassPosition) != null);
                this.emptyEquatorFields -= 1;
            } else if (emptyNotEquatorFields > 0) {
                do {
                    int x = rand.nextInt(mapEdge.getUpperRight().x - mapEdge.getLowerLeft().x + 1) + mapEdge.getLowerLeft().x;
                    int y = rand.nextInt(mapEdge.getUpperRight().y - mapEdge.getLowerLeft().y + 1) + mapEdge.getLowerLeft().y;
                    grassPosition = new Vector2d(x, y);
                } while (grasses.get(grassPosition) != null || (grassPosition.precedes(equatorUpperRight) && grassPosition.follows(equatorLowerLeft)));
                this.emptyNotEquatorFields -= 1;
            } else continue;
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
            Integer genomeCountValue = genomeCount.get(genes);
            genomeCount.put(genes, genomeCountValue == null ? 1 : genomeCountValue + 1);
            Animal animal = new Animal(animalPosition, genes, startingEnergy);
            animals.put(animalPosition, animal);
        }
    }

    @Override
    public void growGrass() {
        Random rand = new Random();
        for (int i = 0; i < grassPerDay; i++) {
            Vector2d grassPosition;
            if (emptyEquatorFields > 0 && rand.nextInt(10) + 1 > 2) {
                do {
                    int x = rand.nextInt(equatorUpperRight.x - equatorLowerLeft.x+1) + equatorLowerLeft.x;
                    int y = rand.nextInt(equatorUpperRight.y - equatorLowerLeft.y+1) + equatorLowerLeft.y;
                    grassPosition = new Vector2d(x, y);
                } while (grasses.get(grassPosition) != null);
                this.emptyEquatorFields -= 1;
            } else if (emptyNotEquatorFields > 0) {
                do {
                    int x = rand.nextInt(mapEdge.getUpperRight().x - mapEdge.getLowerLeft().x + 1) + mapEdge.getLowerLeft().x;
                    int y = rand.nextInt(mapEdge.getUpperRight().y - mapEdge.getLowerLeft().y + 1) + mapEdge.getLowerLeft().y;
                    grassPosition = new Vector2d(x, y);
                } while (grasses.get(grassPosition) != null || (grassPosition.precedes(equatorUpperRight) && grassPosition.follows(equatorLowerLeft)));
                this.emptyNotEquatorFields -= 1;
            } else return;
            grasses.put(grassPosition, new Grass(grassPosition));
        }
    }

    @Override
    public void moveAnimals() {
        for (Animal animal : animals.values()) {
            animals.remove(animal.getPosition(), animal);

            animal.age += 1;
            animal.moveUsingGene();
            animal.setCurrentGene(genome.nextGene(animal.getCurrentGene(), genomeLength));
            mapEdge.crossedEdge(animal);
            animals.put(animal.getPosition(), animal);
        }
    }

    @Override
    public void removeDeadAnimals(int day) {
        for (Animal animal : animals.values()) {
            animal.energy -= energyLostPerDay;
            if (animal.energy <= 0) {
                animal.dayOfDeath = day;
                deadAnimals+=1;
                sumAnimalLifeSpan+=animal.age;
                animals.remove(animal.getPosition(), animal);
                genomeCount.put(animal.getGenes(), genomeCount.get(animal.getGenes()) - 1);
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
            if (vector2d.precedes(equatorUpperRight) && vector2d.follows(equatorLowerLeft)){
                emptyEquatorFields += 1;
            } else{
                emptyNotEquatorFields += 1;
            }
        }
    }

    @Override
    public void copulation() {
        for (Vector2d vector2d : animals.keySet()) {
            if (animals.get(vector2d) != null) {
                ArrayList<Animal> toCopulate = animals.get2Highest(vector2d);
                Animal a = toCopulate.get(0);
                Animal b = toCopulate.get(1);
                if (b != null && a != null && a.energy >= energyToCopulate && b.energy >= energyToCopulate) {
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
                    Integer genomeCountValue = genomeCount.get(genes);
                    genomeCount.put(genes, genomeCountValue == null ? 1 : genomeCountValue + 1);
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
    public MultiMap<Vector2d, Animal> getAnimalsMultiMap(){
        return animals;
    }
    public Collection<Grass> getGrasses() {
        return grasses.values();
    }

    public int getEmptyFields() {
        return emptyEquatorFields + emptyNotEquatorFields;
    }
    public int averageAnimalLifeSpan(){
        if(this.deadAnimals == 0) { return 0;}
        return this.sumAnimalLifeSpan/this.deadAnimals;
    }

    public HashMap<int[], Integer> getGenomeCount() {
        return genomeCount;
    }
}
