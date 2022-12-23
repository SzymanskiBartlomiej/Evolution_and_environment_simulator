package com.project;


import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class SimulationEngine implements IEngine {
    private final IWorldMap map;
    private final int days;
    private final Statistics statistics;
    private final Boolean saveConfig;
    FileWriter outputFile;
    CSVWriter writer;
    public SimulationEngine(IWorldMap iWorldMap, Animal[] animals, int days,Boolean saveConfig) {
        this.map = iWorldMap;
        this.days = days;
        this.saveConfig = saveConfig;
        for (Animal animal : animals) {
            this.map.place(animal);
        }
        this.statistics = new Statistics(map);
        if(saveConfig){
            try {
                String fileName = java.time.LocalDateTime.now().toString() + ".csv";
                this.outputFile = new FileWriter(new File ("SavedStats/" + fileName));
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
            System.out.println(map.toString());
            if(saveConfig){
                saveStatsToCsv(i);
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
}
