package com.project;

public class Grass implements IMapElement {
    private Vector2d vector2d;

    public Grass(Vector2d position) {
        vector2d = position;
    }

    @Override
    public Vector2d getPosition() {
        return vector2d;
    }

    @Override
    public String getTexturePath() {
        return null;
    }

    @Override
    public String toString() {
        return " * ";
    }
}
