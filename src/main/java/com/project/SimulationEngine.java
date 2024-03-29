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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class SimulationEngine implements IEngine,Runnable {
    private final IWorldMap map;
    private final int days;
    public int currDay = 0;
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

        for (currDay=0; currDay <= days; currDay++) {
            System.out.println("day "+ (currDay));
            map.removeDeadAnimals(currDay);
            map.moveAnimals();
            map.eatGrass();
            map.copulation();
            map.growGrass();
            statistics.updateStats();
            if(saveConfig){
                saveStatsToCsv(currDay);
            }
            try{
                CountDownLatch countDownLatch=new  CountDownLatch(1);
                System.out.println((float) Runtime.getRuntime().freeMemory()/Runtime.getRuntime().maxMemory());
                    Platform.runLater(() -> {
                    app.updateMap();
                    app.updateStats();
                    app.updatePlot(currDay);
                    countDownLatch.countDown();
                });
                Thread.sleep(500);
                countDownLatch.await();


            }
            catch (InterruptedException e){
                throw new RuntimeException(e.getMessage());
            }
            while (!running){
                try {
                    Thread.sleep(100);

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
