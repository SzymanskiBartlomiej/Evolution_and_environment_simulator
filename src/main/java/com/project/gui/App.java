package com.project.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.*;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
public class App {
    private IWorldMap map;
    private IMapEdge edges;
    private Statistics statistics;
    private final GridPane gridPane = new GridPane();
    private final VBox stats = new VBox();


    private int days;
    public App(Stage stage,Map<String, Integer> configuration) {

        try {
            //TODO wybór konfiguracji do wczytania lub utworzenie nowej
            //TODO wybór użytkownika czy chce zapisać statystyki symulacji do csv
            ObjectMapper mapper = new ObjectMapper();
            File json = new File("configurationFiles/test1.json");
//            configuration = mapper.readValue(json, Map.class);
            this.edges = new GlobeMapEdge(new Vector2d(configuration.get("width")-1, configuration.get("height")-1));
            this.map = new ForestedEquatorsWorldMap(edges,new BitOfMadnessGenome(),new SlightCorrectionMutation(2, 4),configuration );
            map.populate(configuration.get("numOfAnimals"));
            this.days = configuration.get("days");
            this.statistics = new Statistics(map);
        }  catch (Exception ex) {
            ex.printStackTrace();
        }
        Button stopButton = new Button("Pause");
        drawGrid(gridPane);
        try {
            drawObjects(gridPane);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        updateStats();
        Scene scene = new Scene(new VBox(gridPane,stats,stopButton), 600, 800);
        stage.setScene(scene);
        stage.show();
        IEngine engine = new SimulationEngine(map, new Animal[]{}, days,false,this,statistics);
        Thread engineThread = new Thread(engine::run);
        engineThread.setDaemon(true); //pozwala na zatrzmyanie threaadu przy zamknięciu okna
        engineThread.start();
        //Można to napisać o wiele lepiej no ale coż
        stopButton.setOnAction(event -> {
            engine.changeRunning();
            if (stopButton.getText().equals("Pause")){
                stopButton.setText("Start");
            }else{
                stopButton.setText("Pause");
            }
        });
        //zatrzymywanie threadu przy zamknięciu okna
        stage.setOnCloseRequest(event -> {
            System.out.println("Stage is closing");
            engineThread.interrupt();
        });
    }
    void drawGrid(GridPane gridPane){
        gridPane.setGridLinesVisible(true);
        Vector2d UpperRight = edges.getUpperRight();
        int constraintsX = 600 / (edges.getUpperRight().x + 2);
        int constraintsY = 600 / (edges.getUpperRight().y + 2);
        Label label = new Label("y/x");
        GridPane.setHalignment(label, HPos.CENTER);
        gridPane.add(label,0,0);

        for(int i = 0; i < UpperRight.x+2; i++) {
            if(i!=0){
                Label label2 = new Label(Integer.toString(i-1));
                gridPane.add(label2,i,0);
                GridPane.setHalignment(label2, HPos.CENTER);
            }

            ColumnConstraints column = new ColumnConstraints(constraintsX);
            gridPane.getColumnConstraints().add(column);
        }

        for(int i = 0; i < UpperRight.y+2; i++) {
            if(i!=UpperRight.y+1){
                Label label2 = new Label(Integer.toString(i));
                gridPane.add(label2,0,UpperRight.y + 1 -i);
                GridPane.setHalignment(label2, HPos.CENTER);

            }
            RowConstraints row = new RowConstraints(constraintsY);
            gridPane.getRowConstraints().add(row);
        }
    }
    void drawObjects(GridPane gridPane) throws FileNotFoundException {
        Vector2d UpperRight = edges.getUpperRight();
        for(int i = 0; i < UpperRight.x+1; i++) {
            for(int j = 0; j < UpperRight.y+1; j++) {
                Vector2d currentPosition = new Vector2d(i,j);
                Object object = this.map.objectAt(currentPosition);
                if(object != null && object.getClass() == Grass.class){
                    Image image = new Image(new FileInputStream(((Grass)object).getTexturePath()));
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(20);
                    imageView.setFitHeight(20);
                    VBox vbox = new VBox();
                    vbox.getChildren().add(imageView);
                    gridPane.add(vbox,i+1,UpperRight.y +1 -j);
                    GridPane.setHalignment(vbox, HPos.CENTER);
                }
                if (object instanceof Collection) {
                    ArrayList <Animal> Animals = new ArrayList<>((Collection<Animal>)object);
                    HBox hBox = new HBox();
                    for (Animal animal : Animals){
                        Image image = new Image(new FileInputStream(animal.getTexturePath()));
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(10);
                        imageView.setFitHeight(10);
                        Label energy = new Label(Integer.toString(animal.energy));
                        VBox vbox = new VBox();
                        vbox.getChildren().add(imageView);
                        vbox.getChildren().add(energy);
                        hBox.getChildren().add(vbox);
                    }
                    hBox.setSpacing(2);
                    gridPane.add(hBox,i+1,UpperRight.y +1 -j);
                    hBox.setAlignment(Pos.CENTER);
                    GridPane.setHalignment(hBox, HPos.CENTER);
                }
            }
        }
    }
    public void updateMap(){
        ObservableList<Node> childrens = gridPane.getChildren();
        ArrayList<Node> toRemove = new ArrayList<>();
        for(Node node : childrens){
            if(node instanceof VBox || node instanceof HBox) {
                toRemove.add(node);
            }
        }
        gridPane.getChildren().removeAll(toRemove);
        try {
            drawObjects(gridPane);
        }
        catch (FileNotFoundException e){
            throw new RuntimeException(e.getMessage());
        }
    }
    public void updateStats(){
        this.stats.getChildren().clear();
        Label numOfAnimals = new Label("number of animals: " + statistics.numOfAnimals);
        Label numOfGrasses = new Label("number of grasses: " + statistics.numOfGrasses);
        Label numOfEmptyFields = new Label("number of empty fields: " + statistics.numOfEmptyFields);
        Label mostPopularGenes = new Label("Most popular genes: " + Arrays.toString(statistics.mostPopularGenes));
        Label averageAnimalEnergy = new Label("average animal energy: " + statistics.averageAnimalEnergy);
        Label averageAnimalLifeSpan = new Label("average animal lifespan: " + statistics.averageAnimalLifeSpan);
        this.stats.getChildren().addAll(numOfAnimals,numOfGrasses,numOfEmptyFields,mostPopularGenes,averageAnimalEnergy,averageAnimalLifeSpan);
    }
}
