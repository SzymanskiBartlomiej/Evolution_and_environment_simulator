package com.project;

public class GlobeMapEdge implements IMapEdge{
    public final Vector2d lowerLeft = new Vector2d(0,0);
    public final Vector2d upperRight;
    public GlobeMapEdge(Vector2d upperRight){
        this.upperRight = upperRight;
    }
    // obsługa wyjscia za granice
    @Override
    public void crossedEdge(Animal animal) {
        int x;
        int y;
        if(animal.getPosition().x > upperRight.x){
            x = lowerLeft.x;
        } else if(animal.getPosition().x < lowerLeft.x){
            x = upperRight.x;
        } else {
            x = animal.getPosition().x;
        }
        if(animal.getPosition().y > upperRight.y){
            y = upperRight.y;
            animal.setMapDirection(animal.getMapDirection().turnAround());
        } else if (animal.getPosition().y < lowerLeft.y) {
            y= lowerLeft.y;
            animal.setMapDirection(animal.getMapDirection().turnAround());
        } else {
            y = animal.getPosition().y;
        }
        animal.move(new Vector2d(x,y));
    }
}
