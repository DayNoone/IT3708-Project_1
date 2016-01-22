package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    static Random random = new Random();

    Layer playfield;

    List<Attractor> allAttractors = new ArrayList<>();
    List<Boid> allBoids = new ArrayList<>();

    AnimationTimer gameLoop;

    Vector2D mouseLocation = new Vector2D( 0, 0);

    Scene scene;

    MouseGestures mouseGestures = new MouseGestures();

    Label seperationSliderLabel, alignmentSliderLabel, cohesionSliderLabel;
    Slider speedSlider, seperationSlider, alignmentSlider, cohesionSlider;
    Button button;

    @Override
    public void start(Stage primaryStage) {

        // create containers
        BorderPane root = new BorderPane();

        root.setStyle("application/style.css");

        // Controls
        VBox controlsBox = new VBox(20);
        controlsBox.setId("controllerBox");
        button = new Button("Click Me");
        Label speedLabel = new Label("Speed");
        seperationSliderLabel = new Label("SeperationWeight: " + Settings.SEPERATION_WEIGHT);
        alignmentSliderLabel = new Label("AlignmentWeight: " + Settings.ALIGNMENT_WEIGHT);
        cohesionSliderLabel = new Label("CohesionWeight: " + Settings.COHESION_WEIGHT);
        speedSlider = new Slider(0, 100, Settings.SPRITE_SPEED);
        seperationSlider = new Slider(0, 10, Settings.SEPERATION_WEIGHT);
        alignmentSlider = new Slider(0, 10, Settings.ALIGNMENT_WEIGHT);
        cohesionSlider = new Slider(0, 10, Settings.COHESION_WEIGHT);
        controlsBox.getChildren().addAll(button, speedLabel, speedSlider, seperationSliderLabel, seperationSlider, alignmentSliderLabel, alignmentSlider, cohesionSliderLabel, cohesionSlider);
        root.setLeft(controlsBox);

        // Playfield
        playfield = new Layer( Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
        // entire game as layers
        Pane layerPane = new Pane();
        layerPane.getChildren().addAll(playfield);
        root.setCenter(layerPane);


        scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
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
        for( int i = 0; i < Settings.VEHICLE_COUNT; i++) {
            addVehicles();
        }

        // add attractors
        for( int i = 0; i < Settings.ATTRACTOR_COUNT; i++) {
            addAttractors();
        }


    }

    private void startGame() {

        // start game
        gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {

                // currently we have only 1 attractor
                Attractor attractor = allAttractors.get(0);

                // seek attractor location, apply force to get towards it
                allBoids.forEach(boid -> {
                    //boid.seek( attractor.getLocation());
                    boid.updateVelocity(allBoids);
                });

                // move sprite
                allBoids.forEach(Sprite::move);

                // update in fx scene
                allBoids.forEach(Sprite::display);
                allAttractors.forEach(Sprite::display);

            }
        };

        gameLoop.start();

    }

    /**
     * Add single vehicle to list of vehicles and to the playfield
     */
    private void addVehicles() {

        Layer layer = playfield;

        // random location
        double x = random.nextDouble() * layer.getWidth();
        double y = random.nextDouble() * layer.getHeight();


        double vx = random.nextDouble() * 10 -5;
        double vy = random.nextDouble() * 10 -5;;

        // dimensions
        double width = Settings.BOID_WIDHT;
        double height = width / 2.0;

        // create boid data
        Vector2D location = new Vector2D( x,y);
        Vector2D velocity = new Vector2D( vx,vy);

        // create sprite and add to layer
        Boid boid = new Boid( layer, location, velocity, width, height);

        // register boid
        allBoids.add(boid);

    }

    private void addAttractors() {

        Layer layer = playfield;

        // center attractor
        double x = layer.getWidth() / 2;
        double y = layer.getHeight() / 2;

        // dimensions
        double width = 100;
        double height = 100;

        // create attractor data
        Vector2D location = new Vector2D( x,y);
        Vector2D velocity = new Vector2D( 0,0);
        Vector2D acceleration = new Vector2D( 0,0);

        // create attractor and add to layer
        Attractor attractor = new Attractor( layer, location, velocity, acceleration, width, height);

        // register sprite
        allAttractors.add(attractor);

    }

    private void addListeners() {

        // capture mouse position
        scene.addEventFilter(MouseEvent.ANY, e -> {
            mouseLocation.set(e.getX(), e.getY());
        });

        // Controls listener
        button.setOnAction((event) -> {
            System.out.print("Button Action\n");
        });
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Settings.SPRITE_SPEED = newValue.intValue();
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

        // move attractors via mouse
        for( Attractor attractor: allAttractors) {
            mouseGestures.makeDraggable(attractor);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}