package com.project;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gui.App;
import com.project.gui.Menu;
import javafx.application.Application;

import java.io.File;
import java.util.HashMap;

public class World {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        File json = new File("configurationFiles/test1.json");

        // bez zapisywania statystyk
//        try {
//            Map<String, Integer> jsonConfiguration = mapper.readValue(json, Map.class);
//            IMapEdge edges = new GlobeMapEdge(new Vector2d(jsonConfiguration.get("width")-1, jsonConfiguration.get("height")-1));
//            IWorldMap map = new ForestedEquatorsWorldMap(edges,new BitOfMadnessGenome(),new SlightCorrectionMutation(2, 4),jsonConfiguration );
//            map.populate(jsonConfiguration.get("numOfAnimals"));
//            IEngine engine = new SimulationEngine(map, new Animal[]{}, jsonConfiguration.get("days"),false);
//            engine.run();
//        }  catch (Exception ex) {
//            ex.printStackTrace();
//        }
        //z zapisywaniem statystyk
//        try {
//            Map<String, Integer> jsonConfiguration = mapper.readValue(json, Map.class);
//            IMapEdge edges = new GlobeMapEdge(new Vector2d(jsonConfiguration.get("width")-1, jsonConfiguration.get("height")-1));
//            IWorldMap map = new ForestedEquatorsWorldMap(edges,new BitOfMadnessGenome(),new SlightCorrectionMutation(2, 4),jsonConfiguration );
//            map.populate(jsonConfiguration.get("numOfAnimals"));
//            IEngine engine = new SimulationEngine(map, new Animal[]{}, jsonConfiguration.get("days"),true);
//            engine.run();
//        }  catch (Exception ex) {
//            ex.printStackTrace();
//        }
        //Application.launch(App.class, args);
        Application.launch(Menu.class,args);
    }
}
