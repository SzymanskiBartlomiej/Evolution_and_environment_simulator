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
        return switch (this) {
            case N -> new Vector2d(0, 1);
            case NE -> new Vector2d(1, 1);
            case E -> new Vector2d(1, 0);
            case SE -> new Vector2d(1, -1);
            case S -> new Vector2d(0, -1);
            case SW -> new Vector2d(-1, -1);
            case W -> new Vector2d(-1, 0);
            case NW -> new Vector2d(-1, 1);
            default -> new Vector2d(0, 0);
        };
    }
    public MapDirection turnAround(){
        MapDirection direction = DEFAULT;
        switch (this){
            case N -> direction = S;
            case NE -> direction = SW;
            case E -> direction = W;
            case SE -> direction = NW;
            case S -> direction = N;
            case SW -> direction = NE;
            case W -> direction = E;
            case NW -> direction = SE;
        }
        return direction;
    }
}
