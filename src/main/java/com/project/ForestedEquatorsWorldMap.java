package com.project;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ForestedEquatorsWorldMap implements IWorldMap{
    private final int grassEnergy; //energia z 1 trawy
    private final int grassPerDay; //ilośc trawy która rośnie co dziennie
    //parametry startowe ^^^^ brakuje sporo
    private final IMapEdge mapEdge;
    private final Map<Vector2d, Grass> grasses = new HashMap<>();
    private final MultiMap<Vector2d, ArrayList<Animal>> animals = new MultiMap<>();
    private final Vector2d equatorLowerLeft; // wystarczy może equatorWidth bo i tak wiemy ze ma być na srodku mapy i przez cało jej długość
    private final Vector2d equatorUpperRight;
    public ForestedEquatorsWorldMap(IMapEdge mapEdge,
                                    Vector2d equatorLowerLeft,Vector2d equatorUpperRight,int grassEnergy,
                                    int grassPerDay) {
        this.mapEdge = mapEdge;
        this.equatorLowerLeft = equatorLowerLeft;
        this.equatorUpperRight = equatorUpperRight;
        this.grassEnergy = grassEnergy;
        this.grassPerDay = grassPerDay;
    }
    @Override
    public void growGrass() {
        for (int i = 0; i < grassPerDay; i++){
        }
    }

    @Override
    public void moveAnimals() {

    }

    @Override
    public void removeDeadAnimals() {

    }

    @Override
    public void eatGrass() {

    }

    @Override
    public void copulation() {

    }

}
