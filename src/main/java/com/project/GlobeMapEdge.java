package com.project;

public class GlobeMapEdge implements IMapEdge{
    public final Vector2d lowerLeft;
    public final Vector2d upperRight;
    public GlobeMapEdge(Vector2d lowerLeft, Vector2d upperRight){
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
    }
    // obsługa wyjscia za granice
    @Override
    public Vector2d crossedEdge(Vector2d position) {
        int x;
        int y;
        if(position.x > upperRight.x){
            x = lowerLeft.x;
        } else if(position.x < lowerLeft.x){
            x = upperRight.x;
        } else {
            x = position.x;
        }
        if(position.y > upperRight.y){
            y = upperRight.y;
        } else if (position.y < lowerLeft.y) {
            y= lowerLeft.y;
        } else {
            y = position.y;
        }
        return new Vector2d(x,y);
    }
}
