package com.project;


import com.opencsv.CSVWriter;
import com.project.gui.App;
import javafx.application.Platform;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class SimulationEngine implements IEngine,Runnable {
    private final IWorldMap map;
    private final int days;

    private final Statistics statistics;
    private final Boolean saveConfig;
    private final App app;
    FileWriter outputFile;
    CSVWriter writer;
    private boolean running = true;
    public SimulationEngine(IWorldMap iWorldMap, Animal[] animals, int days,Boolean saveConfig, App app,Statistics statistics) {
        this.map = iWorldMap;
        this.days = days;
        this.saveConfig = saveConfig;
        this.app = app;
        for (Animal animal : animals) {
            this.map.place(animal);
        }
        this.statistics = statistics;
        if(saveConfig){
            try {
                String fileName = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()) + ".csv";
                File file = new File("SavedStats");
                if (!file.exists()) {
                    file.mkdir();
                }
                this.outputFile = new FileWriter("SavedStats/" + fileName);
                this.writer = new CSVWriter(outputFile);
                String[] header = { "numOfAnimals", "numOfGrasses", "numOfEmptyFields","mostPopularGenes" , "averageAnimalEnergy" , "averageAnimalLifeSpan" };
                writer.writeNext(header);
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        for (int i = days; i > 0; i--) {
            System.out.println("day "+ (days - i));
            map.removeDeadAnimals();
            map.moveAnimals();
            map.eatGrass();
            map.copulation();
            map.growGrass();
            statistics.updateStats();
            if(saveConfig){
                saveStatsToCsv(i);
            }
            try{
                Thread.sleep(1000);
                System.out.println(Long.toString(Runtime.getRuntime().freeMemory()) + ' ' + Long.toString(Runtime.getRuntime().totalMemory()) +' ' + Long.toString(Runtime.getRuntime().maxMemory()));
                Platform.runLater(() -> app.updateMap());
                Platform.runLater(() -> app.updateStats());

            }
            catch (InterruptedException e){
                throw new RuntimeException(e.getMessage());
            }
            while (!running){
                try {
                    Thread.sleep(100);
                    System.out.println(Long.toString(Runtime.getRuntime().freeMemory()) + ' ' + Long.toString(Runtime.getRuntime().totalMemory()) +' ' + Long.toString(Runtime.getRuntime().maxMemory()));

                }catch (InterruptedException e){
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        if(saveConfig){
            try {
                this.writer.close();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
    public void saveStatsToCsv(int daysLeft){
        String[] stats = {
                String.valueOf(statistics.numOfAnimals),
                String.valueOf(statistics.numOfGrasses),
                String.valueOf(statistics.numOfEmptyFields),
                Arrays.toString(statistics.mostPopularGenes),
                String.valueOf(statistics.averageAnimalEnergy),
                String.valueOf(statistics.averageAnimalLifeSpan)};
        writer.writeNext(stats);
    }
    public void changeRunning(){
        running = !running;
    }
}
