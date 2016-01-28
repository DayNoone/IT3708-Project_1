package application;


public class Settings {

    public static final double OBSTACLE_SIZE = 50;
    public static final double PREDATOR_WIDTH = 50;
    public static final double PREDATOR_SPEED = 5;
    public static double BOID_WIDHT = 50;
    public static double SCENE_WIDTH = 1800;
    public static double SCENE_HEIGHT = 800;

    public static double SEPERATION_WEIGHT = 1.5;
    public static double ALIGNMENT_WEIGHT = 1;
    public static double COHESION_WEIGHT = 1;

    public static double NEIGHBOUR_RADIUS = 50;


    public static int ATTRACTOR_COUNT = 1;
    public static int VEHICLE_COUNT = 200;

    public static double SPRITE_SPEED = 5;
    public static double SPRITE_MAX_FORCE = 0.1;

    // distance at which the sprite moves slower towards the target 
    public static double SPRITE_SLOW_DOWN_DISTANCE = 100;

}