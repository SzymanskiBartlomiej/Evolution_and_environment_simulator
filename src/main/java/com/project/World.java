package com.project;

public class World {
    public static void main(String[] args) {
        MultiMap<Vector2d,Integer> mapa = new MultiMap<>();
        mapa.put(new Vector2d(1,1),5);
        mapa.put(new Vector2d(1,1),3);
        mapa.put(new Vector2d(1,4),2);
        mapa.put(new Vector2d(1,4),5);
        mapa.put(new Vector2d(1,1),5);
        mapa.put(new Vector2d(1,1),5);
        System.out.println(mapa.get(new Vector2d(1,1)));
        System.out.println(mapa.values());
}}
