package application;

import javafx.scene.Node;

import java.util.List;

public class Predator extends Sprite {
    public Predator(Layer layer, Vector2D location, Vector2D velocity, double width, double height) {
        super(layer, location, velocity, width, height);
    }

    @Override
    public Node createView() {
        return null;
    }

    @Override
    public void updateVelocity(List<Boid> allBoids, List<Obstacle> allObstacles) {

    }
}
