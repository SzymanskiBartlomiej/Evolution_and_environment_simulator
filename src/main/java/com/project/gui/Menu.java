package com.project.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.util.*;

public class Menu extends Application{
    private final ArrayList<TextField> textFields = new ArrayList<>();
    private Map<String, Integer> configuration = new HashMap<>();
    private final String[] labelText = new String[]{
            "Map Height",
            "Map Width",
            "Equator Height",
            "Energy From Grass",
            "Grass Per Day",
            "Energy Lost Per Day",
            "Energy Needed To Copulate",
            "Energy Used To Copulate",
            "Starting Energy",
            "Length Of Genome",
            "Starting Number Of Grass",
            "Starting Number Of Animals",
            "Number Of Days"};//TODO: Usunąć ograniczenie dni
    private final String[] keys = new String[]{
            "height",
            "width",
            "equatorHeight",
            "grassEnergy",
            "grassPerDay",
            "energyLostPerDay",
            "energyToCopulate",
            "energyLostToCopulate",
            "startingEnergy",
            "genomeLength",
            "startingGrassNum",
            "numOfAnimals",
            "days"};
    @Override
    public void start(Stage primaryStage) {
        ArrayList<Label> labels = new ArrayList<>();
        VBox vBox = new VBox();
        for(int i = 0;i<labelText.length;i++){
            labels.add(new Label(labelText[i]));
            textFields.add(new TextField());
            vBox.getChildren().add(labels.get(i));
            vBox.getChildren().add(textFields.get(i));
        }
        Button openButton = new Button("Read from a json file");
        FileChooser fileChooser = new FileChooser();
        ObjectMapper mapper = new ObjectMapper();
        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(primaryStage);
                        if (file != null) {
                            try {
                                configuration = mapper.readValue(file, Map.class);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            for(int i = 0;i<keys.length;i++){
                                textFields.get(i).setText(Integer.toString(configuration.get(keys[i])));;
                            }
                            System.out.println(configuration.toString());

                        }
                    }
                });
        vBox.getChildren().add(openButton);
        Button newSimulationButton = new Button("Start");
        vBox.getChildren().add(newSimulationButton);
        Scene scene = new Scene(vBox, 400, 620);
        primaryStage.setScene(scene);
        primaryStage.show();
        newSimulationButton.setOnAction(event -> {
            for(int i = 0;i<keys.length;i++){
                configuration.put(keys[i],Integer.parseInt(textFields.get(i).getText()));
            }
            newSimulation();
        });
    }

    private void newSimulation(){
        Stage stage = new Stage();
        App app = new App(stage,configuration);
        System.out.println("continuing");
        }
    }
