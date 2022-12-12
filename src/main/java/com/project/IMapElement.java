package com.project;

public interface IMapElement {

    /**
     * Returns position of an object.
     *
     * @return Position of an object.
     */
    Vector2d getPosition();

    /**
     * Returns string conversion of the element
     *
     * @return String conversion of an element.
     */
    String toString();
    /**
     * Returns path to texture of an element
     *
     * @return String path to texture of an element.
     */
    String getTexturePath();

}
