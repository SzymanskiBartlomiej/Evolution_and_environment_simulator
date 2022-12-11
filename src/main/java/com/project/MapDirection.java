package com.project;

public enum MapDirection {
    // N - > North NE->North East ...
    N, NE, E, SE, S, SW,W,NW,DEFAULT;
    public MapDirection next(){
        MapDirection direction = DEFAULT;
        switch (this){
            case N -> direction = NE;
            case NE -> direction = E;
            case E -> direction = SE;
            case SE -> direction = S;
            case S -> direction = SW;
            case SW -> direction = W;
            case W -> direction = NW;
            case NW -> direction = N;
        }
        return direction;
    }
    public Vector2d toUnitVector(){
        switch (this){
            case N:
                return new Vector2d(0,1);
            case NE:
                return new Vector2d(1,1);
            case E:
                return new Vector2d(1,0);
            case SE:
                return new Vector2d(1,-1);
            case S:
                return new Vector2d(0,-1);
            case SW:
                return new Vector2d(-1,-1);
            case W:
                return new Vector2d(-1,0);
            case NW:
                return new Vector2d(-1,1);
            default:
                return new Vector2d(0,0);
        }
    }
}
