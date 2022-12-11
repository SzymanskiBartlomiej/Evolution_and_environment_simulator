package com.project;

import java.util.Objects;

public class Vector2d {
    int x;
    int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString(){
        String res = new String("(" + x + "," + y + ")");
        return res;
    }
    public Vector2d add(Vector2d other){
        Vector2d sumVector = new Vector2d(x + other.x , y + other.y);
        return sumVector;
    }
    public boolean precedes(Vector2d other){
        if(x<=other.x && y<=other.y){
            return true;
        }
        else {
            return false;
        }
    }
    public boolean follows(Vector2d other){
        if(x>=other.x && y>=other.y){
            return true;
        }
        else {
            return false;
        }
    }
    public Vector2d subtract(Vector2d other){
        Vector2d subtractVector = new Vector2d(x - other.x , y - other.y);
        return subtractVector;
    }
    public Vector2d upperRight(Vector2d other){
        Vector2d upperRightVector = new Vector2d(Math.max(x,other.x) , Math.max(y,other.y));
        return upperRightVector;
    }
    public Vector2d lowerLeft(Vector2d other){
        Vector2d lowerLeftVector = new Vector2d(Math.min(x,other.x) , Math.min(y,other.y));
        return lowerLeftVector;
    }
    public Vector2d opposite(){
        Vector2d oppositeVector = new Vector2d(x*(-1),y*(-1));
        return oppositeVector;
    }
    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        if (that.x == x && that.y == y){
            return true;
        }
        else{
            return false;
        }
    }
    public int hashCode(){
        return Objects.hash(this.x,this.y);
    }
}
