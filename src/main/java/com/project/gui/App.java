package com.project.gui;

import com.project.*;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
public class App {
    private IWorldMap map;
    private IMapEdge edges;
    private Statistics statistics;
    private IEngine engine;
    private final GridPane gridPane = new GridPane();
    private final VBox stats = new VBox();
    private final XYChart.Series<Integer,Number> seriesGrass = new XYChart.Series<>();
    private final XYChart.Series<Integer,Number>seriesAnimals = new XYChart.Series<>();

    private LineChart lineChart;
    private int days;
    private Animal observedAnimal;
    public App(Stage stage,Map<String, Integer> configuration,boolean saveStats) {

        try {

            this.edges = new GlobeMapEdge(new Vector2d(configuration.get("width")-1, configuration.get("height")-1));
            this.map = new ForestedEquatorsWorldMap(edges,new BitOfMadnessGenome(),new SlightCorrectionMutation(2, 4),configuration );
            map.populate(configuration.get("numOfAnimals"));
            this.days = configuration.get("days");
            this.statistics = new Statistics(map);
        }  catch (Exception ex) {
            ex.printStackTrace();
        }
        ToggleButton stopButton = new ToggleButton("Pause");
        drawGrid(gridPane);
        try {
            drawObjects(gridPane);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        updateStats();
        createPlot();
        lineChart.setPrefWidth(450);
        HBox hbox = new HBox(stats,lineChart);
        Scene scene = new Scene(new VBox(gridPane,hbox,stopButton), 800, 900);
        stage.setScene(scene);
        stage.show();
        engine = new SimulationEngine(map, new Animal[]{}, days,saveStats,this,statistics);
        Thread engineThread = new Thread(engine::run);
        //zatrzmyanie threaadu przy zamknięciu okna
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                engineThread.interrupt();
            }
        });
        engineThread.start();
        stopButton.setOnAction(event -> {
            engine.changeRunning();
            if (stopButton.isSelected()){
                highlightMostPopularGene("src/main/resources/animalHighlited.png");
                gridPane.setDisable(false);
                gridPane.setOnMouseClicked( ( e ) ->
                {
                    observeAnimal(e);
                } );
                stopButton.setText("Start");
            }else{
                stopButton.setText("Pause");
                gridPane.setDisable(true);
                highlightMostPopularGene("src/main/resources/animal.png");
            }
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
        for (int i = 0; i < UpperRight.x + 1; i++) {
            for (int j = 0; j < UpperRight.y + 1; j++) {
                Vector2d currentPosition = new Vector2d(i, j);
                Object object = this.map.objectAt(currentPosition);
                if (object != null && object.getClass() == Grass.class) {
                    Image image = new Image(new FileInputStream(((Grass) object).getTexturePath()));
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(20);
                    imageView.setFitHeight(20);
                    gridPane.add(imageView, i + 1, UpperRight.y + 1 - j);
                    GridPane.setHalignment(imageView, HPos.CENTER);
                }
                if (object instanceof Collection) {
                    ArrayList<Animal> Animals = new ArrayList<>((Collection<Animal>) object);
                    HBox hBox = new HBox();
                    for (Animal animal : Animals) {
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
                    gridPane.add(hBox, i + 1, UpperRight.y + 1 - j);
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
            if(node instanceof HBox || node instanceof ImageView) {
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
        Label numOfAnimals = new Label("num of animals: " + statistics.numOfAnimals);
        Label numOfGrasses = new Label("num of grasses: " + statistics.numOfGrasses);
        Label numOfEmptyFields = new Label("num of empty fields: " + statistics.numOfEmptyFields);
        Label mostPopularGenes = new Label("Most popular genes: " +"\n"+ Arrays.toString(statistics.mostPopularGenes)
                + " in " + map.getGenomeCount().get(statistics.mostPopularGenes) + " animals");
        Label averageAnimalEnergy = new Label("avg. animal energy: " + statistics.averageAnimalEnergy);
        Label averageAnimalLifeSpan = new Label("avg. animal lifespan: " + statistics.averageAnimalLifeSpan);
        if(observedAnimal == null){
            this.stats.getChildren().addAll(numOfAnimals,numOfGrasses,numOfEmptyFields,mostPopularGenes,averageAnimalEnergy,averageAnimalLifeSpan);
        }
        else{
            Label ObservedAnimalEnergy = new Label("\n Observed Animal Energy: " + observedAnimal.energy);
            Label ObservedAnimalGenes = new Label("Observed Animal Genes: " + Arrays.toString(observedAnimal.getGenes()));
            Label ObservedAnimalCurrentGene = new Label("Observed Animal Current Gene: " + observedAnimal.getCurrentGene());
            Label ObservedAnimalChildrens = new Label("Observed Animal Childrens: " + observedAnimal.getNumOfChildren());
            Label ObservedAnimalAge= new Label("Observed Animal Age: " + observedAnimal.getAge());
            Label ObservedAnimalDayOfDeath= new Label("Observed Animal Day Of Death: " + observedAnimal.getDayOfDeath());
            Button stopObserving = new Button("stopObserving (works on pause)");
            stopObserving.setOnAction(event -> {
                this.observedAnimal = null;
                updateStats();
            });
            this.stats.getChildren().addAll(numOfAnimals,numOfGrasses,numOfEmptyFields,mostPopularGenes,
                    averageAnimalEnergy,averageAnimalLifeSpan,ObservedAnimalEnergy,ObservedAnimalGenes,
                    ObservedAnimalCurrentGene,ObservedAnimalChildrens,ObservedAnimalAge,ObservedAnimalDayOfDeath,stopObserving);
        }
    }

    private void createPlot(){
        //TODO: nazwy serii
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Day");
        NumberAxis yAxis = new NumberAxis();
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setLegendVisible(true);
        seriesAnimals.setName("Number of Animals");
        seriesGrass.setName("Number of Grass");
        seriesGrass.getData().add(new XYChart.Data<>(0,statistics.numOfGrasses));
        seriesAnimals.getData().add(new XYChart.Data<>(0,statistics.numOfAnimals));
        lineChart.getData().addAll(seriesGrass,seriesAnimals);
    }
    public void updatePlot(int day){
        seriesAnimals.getData().add(new XYChart.Data<>(day,statistics.numOfAnimals));
        seriesGrass.getData().add(new XYChart.Data<>(day,statistics.numOfGrasses));
    }
    public void observeAnimal(javafx.scene.input.MouseEvent e) {
        Node clickedNode = e.getPickResult().getIntersectedNode().getParent();
        if (clickedNode == null || clickedNode.getParent() == null) {
            return;
        }
        if (gridPane.getColumnIndex(clickedNode.getParent()) != null && gridPane.getRowIndex(clickedNode.getParent()) != null) {
            int colIndex = GridPane.getColumnIndex(clickedNode.getParent()) - 1;
            int rowIndex = edges.getUpperRight().y + 1 - GridPane.getRowIndex(clickedNode.getParent());
            Object object = this.map.objectAt(new Vector2d(colIndex, rowIndex));
            int i = 0;
            for (Node node : clickedNode.getParent().getChildrenUnmodifiable()) {
                i += 1;
                if (node == clickedNode) {
                    break;
                }
            }
            Animal animal = new ArrayList<>((Collection<Animal>) object).get(i - 1);
            this.observedAnimal = animal;
            updateStats();
        }
    }
    public void highlightMostPopularGene(String path){
        Collection<Animal> animals = map.getAnimals();
        int[] mostPopularGenes = statistics.mostPopularGenes;
        for (Animal animal : animals){
            if(animal.getGenes() == mostPopularGenes){
                animal.setTexturePath(path);
            }
        }
    }
}
