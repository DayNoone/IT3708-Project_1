package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    static Random random = new Random();

    Layer playfield;

    List<Obstacle> allObstacles = new ArrayList<Obstacle>();
    List<Boid> allBoids = new ArrayList<>();
    List<Predator> allPredators = new ArrayList<>();

    AnimationTimer gameLoop;

    Vector2D mouseLocation = new Vector2D( 0, 0);

    Scene scene;

    Label seperationSliderLabel, alignmentSliderLabel, cohesionSliderLabel, predatorSeperationSliderLabel, obstacleSizeSliderLabel;
    Slider speedSlider, seperationSlider, alignmentSlider, cohesionSlider, predatorSeperationSlider, obstacleSizeSlider;

    @Override
    public void start(Stage primaryStage) {

        // create containers
        BorderPane root = new BorderPane();

        root.setStyle("application/style.css");

        // Controls
        VBox controlsBox = new VBox(20);
        controlsBox.setAlignment(Pos.TOP_CENTER);
        controlsBox.setMaxWidth(Settings.CONTROLS_WIDTH);
        controlsBox.setId("controllerBox");
        Label speedLabel = new Label("Speed");
        seperationSliderLabel = new Label("SeperationWeight: " + Settings.SEPERATION_WEIGHT);
        alignmentSliderLabel = new Label("AlignmentWeight: " + Settings.ALIGNMENT_WEIGHT);
        cohesionSliderLabel = new Label("CohesionWeight: " + Settings.COHESION_WEIGHT);
        predatorSeperationSliderLabel = new Label("Predator SeperationWeight: " + Settings.PREDATOR_SEPERATION_WEIGHT);
        obstacleSizeSliderLabel = new Label("Obstacle Size: " + Settings.OBSTACLE_SIZE);
        speedSlider = new Slider(0, 50, Settings.BOID_SPEED);
        seperationSlider = new Slider(1, 10, Settings.SEPERATION_WEIGHT);
        seperationSlider.setBlockIncrement(1);
        alignmentSlider = new Slider(1, 10, Settings.ALIGNMENT_WEIGHT);
        alignmentSlider.setBlockIncrement(1);
        cohesionSlider = new Slider(1, 10, Settings.COHESION_WEIGHT);
        cohesionSlider.setBlockIncrement(1);
        predatorSeperationSlider = new Slider(1, 30, Settings.PREDATOR_SEPERATION_WEIGHT);
        predatorSeperationSlider.setBlockIncrement(1);
        obstacleSizeSlider = new Slider(1, 140, Settings.OBSTACLE_SIZE);
        root.setLeft(controlsBox);

        // Playfield
        playfield = new Layer(Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
        // entire game as layers
        Pane layerPane = new Pane();
        layerPane.getChildren().addAll(playfield);
        root.setRight(layerPane);


        scene = new Scene(root, Settings.SCENE_WIDTH+Settings.CONTROLS_WIDTH, Settings.SCENE_HEIGHT);
        File f = new File("src/application/style.css");
        scene.getStylesheets().clear();
        scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        primaryStage.setScene(scene);
        primaryStage.show();

        // add content
        prepareGame();

        // add mouse location listener
        addListeners();

        // run animation loop
        startGame();


    }

    private void prepareGame() {

        // add vehicles
        for( int i = 0; i < Settings.BOID_COUNT; i++) {
            addBoids();
        }
    }

    private void startGame() {

        // start game
        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {

                allBoids.forEach(boid -> {
                    boid.updateVelocity(allBoids, allObstacles, allPredators);
                });
                allPredators.forEach(predator -> {
                    predator.updateVelocity(allBoids, allObstacles, allPredators);
                });

                // move sprite
                allBoids.forEach(Sprite::move);
                allPredators.forEach(Sprite::move);

                // update in fx scene
                allBoids.forEach(Sprite::display);
                allPredators.forEach(Sprite::display);
                allObstacles.forEach(Sprite::display);

            }
        };

        gameLoop.start();

    }

    /**
     * Add single vehicle to list of vehicles and to the playfield
     */
    private void addBoids() {

        Layer layer = playfield;

        // random location
        double x = random.nextDouble() * layer.getWidth();
        double y = random.nextDouble() * layer.getHeight();


        double vx = random.nextDouble() * 10 -5;
        double vy = random.nextDouble() * 10 -5;;

        // dimensions
        double width = Settings.BOID_WIDTH;
        double height = width / 2.0;

        // create boid data
        Vector2D location = new Vector2D( x,y);
        Vector2D velocity = new Vector2D( vx,vy);

        // create sprite and add to layer
        Boid boid = new Boid( layer, location, velocity, width, height);

        // register boid
        allBoids.add(boid);

    }

    private void addObstacles(KeyEvent event) {
        Layer layer = playfield;

        Vector2D location = new Vector2D(mouseLocation.x, mouseLocation.y);

        double radius = Settings.OBSTACLE_SIZE;

        System.out.println("Obstacle @ location:  X: " + location.x + " Y:  " + location.y);

        Obstacle obstacle = new Obstacle(layer, location, radius);

        obstacle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(allObstacles);
                allObstacles.remove(obstacle);
                obstacle.remove();
                System.out.println(allObstacles);
            }
        });

        allObstacles.add(obstacle);
    }

    private void addPredators(KeyEvent event) {
        Layer layer = playfield;

        Vector2D location = new Vector2D(mouseLocation.x, mouseLocation.y);

        double width = Settings.PREDATOR_WIDTH;
        double height = width / 2.0;

        System.out.println("Predator @ location:  X: " + location.x + " Y:  " + location.y);

        Predator predator = new Predator(layer, location, width, height);

        predator.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(allPredators);
                allPredators.remove(predator);
                predator.remove();
                System.out.println(allPredators);
            }
        });

        allPredators.add(predator);
    }

    private void addListeners() {

        // capture mouse position
        playfield.addEventFilter(MouseEvent.ANY, e -> {
            mouseLocation.set(e.getX(), e.getY());
        });

        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Settings.BOID_SPEED = newValue.intValue();
        });
        seperationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("seperationSlider Value Changed (newValue: " + newValue.intValue() + ")");
            Settings.SEPERATION_WEIGHT = newValue.intValue();
            seperationSliderLabel.setText("SeperationWeight: " + Settings.SEPERATION_WEIGHT);
        });
        alignmentSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Settings.ALIGNMENT_WEIGHT = newValue.intValue();
            alignmentSliderLabel.setText("AlignmentWeight: " + Settings.ALIGNMENT_WEIGHT);
        });
        cohesionSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Settings.COHESION_WEIGHT = newValue.intValue();
            cohesionSliderLabel.setText("CohesionWeight: " + Settings.COHESION_WEIGHT);
        });
        predatorSeperationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("predatorSeperationSlider Value Changed (newValue: " + newValue.intValue() + ")");
            Settings.PREDATOR_SEPERATION_WEIGHT = newValue.intValue();
            predatorSeperationSliderLabel.setText("SeperationWeight: " + Settings.PREDATOR_SEPERATION_WEIGHT);
        });
        obstacleSizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Obstacle Size Value Changed (newValue: " + newValue.intValue() + ")");
            Settings.OBSTACLE_SIZE = newValue.intValue();
            obstacleSizeSliderLabel.setText("Obstacle size: " + Settings.OBSTACLE_SIZE);
        });

        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.O) {
                addObstacles(e);
            } else if (e.getCode() == KeyCode.P) {
                addPredators(e);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}