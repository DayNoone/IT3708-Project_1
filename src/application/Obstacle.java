package application;

import javafx.scene.Node;

import java.util.List;

public class Obstacle extends Sprite {
    public Obstacle(Layer layer, Vector2D location, double radius) {
        super(layer, location, new Vector2D(0, 0), radius, radius);
    }

    @Override
    public Node createView() {
        return Utils.createObstacleImageView((int) width);
    }

    @Override
    public void updateVelocity(List<Boid> allBoids) {
    }
}
