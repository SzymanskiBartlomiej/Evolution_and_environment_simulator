package com.project;

import java.util.Objects;

public class Vector2d {
    public final int x; //nie wiem nie prościej tu dać po prostu public int x i bez metod getX() ???
    public final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(x + other.x, y + other.y);
    }

    public boolean precedes(Vector2d other) {
        return x <= other.x && y <= other.y;
    }

    public boolean follows(Vector2d other) {
        return x >= other.x && y >= other.y;
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(x - other.x, y - other.y);
    }

    public Vector2d upperRight(Vector2d other) {
        return new Vector2d(Math.max(x, other.x), Math.max(y, other.y));
    }

    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(Math.min(x, other.x), Math.min(y, other.y));
    }

    public Vector2d opposite() {
        return new Vector2d(x * (-1), y * (-1));
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d that))
            return false;
        return that.x == x && that.y == y;
    }

    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}
