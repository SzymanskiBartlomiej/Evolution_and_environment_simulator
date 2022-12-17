package com.project;
import java.util.*;

public class ForestedEquatorsWorldMap implements IWorldMap{
    private final int grassEnergy; //energia z 1 trawy
    private final int energyLostPerDay; // dzienna utrata energii
    private final int grassPerDay; //ilośc trawy która rośnie co dziennie
    //parametry startowe ^^^^ brakuje sporo
    private final IMapEdge mapEdge;
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final MultiMap<Vector2d, Animal> animals = new MultiMap<>();
    private final Vector2d equatorLowerLeft; // wystarczy może equatorWidth bo i tak wiemy ze ma być na srodku mapy i przez cało jej długość
    private final Vector2d equatorUpperRight;
    private int emptyEquatorFields;
    private final Vector2d mapLowerLeft;
    private final Vector2d mapUpperRight;
    public ForestedEquatorsWorldMap(
            IMapEdge mapEdge, Vector2d equatorLowerLeft,
            Vector2d equatorUpperRight,int grassEnergy, int grassPerDay , int energyLostPerDay) {
        this.mapEdge = mapEdge;
        this.equatorLowerLeft = equatorLowerLeft;
        this.equatorUpperRight = equatorUpperRight;
        this.grassEnergy = grassEnergy;
        this.grassPerDay = grassPerDay;
        this.mapLowerLeft = mapEdge.getLowerLeft();
        this.mapUpperRight = mapEdge.getUpperRight();
        this.emptyEquatorFields = (equatorUpperRight.x-equatorLowerLeft.x)*(equatorUpperRight.y-equatorLowerLeft.y);
        this.energyLostPerDay = energyLostPerDay;
    }
    @Override
    public void growGrass() {
        Random rand = new Random();
        for (int i = 0; i < grassPerDay; i++){
            Vector2d grassPosition ;
            // losowanie trawy na tym równiku nie ma sensu bo jak zostało tylko 1 pole to trwa to w nieskończoność??
            if(emptyEquatorFields>0 && rand.nextInt(10)+1>2){
                do {
                    int x = rand.nextInt(equatorUpperRight.x-equatorLowerLeft.x)+equatorLowerLeft.x;
                    int y = rand.nextInt(equatorUpperRight.y-equatorLowerLeft.y)+equatorLowerLeft.y;
                    grassPosition = new Vector2d(x,y);
                }while (grasses.get(grassPosition)!=null);
                this.emptyEquatorFields-=1;
            }
            else{
                do {
                    int x = rand.nextInt(mapUpperRight.x-mapLowerLeft.x)+mapLowerLeft.x;
                    int y = rand.nextInt(mapUpperRight.y-mapLowerLeft.y)+mapLowerLeft.y;
                    grassPosition = new Vector2d(x,y);
                }while (grasses.get(grassPosition)!=null);
            }
            grasses.put(grassPosition,new Grass(grassPosition));
        }
    }

    @Override
    public void moveAnimals() {
        /// TODO: observer do animals (multimap)
        for (Animal animal : animals.values()){
            animals.remove(animal.getPosition(),animal);

            animal.energy -= energyLostPerDay;
            animal.age += 1;

            Vector2d oldPosition = animal.getPosition();
            animal.moveUsingGene();
            mapEdge.crossedEdge(animal);
            animals.put(animal.getPosition(),animal);
        }
    }

    @Override
    public void removeDeadAnimals() {
        for (Animal animal : animals.values()){
            if(animal.energy<=0){
                animals.remove(animal.getPosition(),animal);
            }
        }
    }

    @Override
    public void eatGrass() {
        List<Vector2d> vector2dsToDelete = new ArrayList<>();
        for (Vector2d vector2d : this.grasses.keySet()){
            if(animals.get(vector2d)!=null){
                System.out.println("eating grass");
                selectStrongestAnimal(vector2d).energy += grassEnergy;
                vector2dsToDelete.add(vector2d);
            }
        }
        for (Vector2d vector2d : vector2dsToDelete){
            grasses.remove(vector2d);
        }
    }

    @Override
    public void copulation() {
    }

    @Override
    public void place(Animal animal) {
        Vector2d position = animal.getPosition();
        animals.put(position,animal);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        if(objectAt(position)!=null){
            return true;
        }
        return false;
    }

    @Override
    public Object objectAt(Vector2d position) {
        if (animals.get(position)!=null){
            // zwraca ArrayListe
            return animals.get(position);
        }
        return grasses.get(position);
    }

    @Override
    public String toString(){
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(mapLowerLeft,mapUpperRight);
    }

    public Animal selectStrongestAnimal(Vector2d vector2d){
        //TODO selects strongest animal on vector.
        return animals.get(vector2d).last();
    }
    public Animal[] select2StrongestAnimal(Vector2d vector2d){
        Iterator <Animal> iterator = animals.get(vector2d).descendingIterator();
        Animal animal1 = iterator.next();
        Animal animal2 = iterator.next();
        return new Animal[]{animal1,animal2};
    }

    public Collection<Animal> getAnimals() {
        return animals.values();
    }
}
