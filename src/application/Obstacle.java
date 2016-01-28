package application;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;

public class Obstacle extends Sprite {
    public Obstacle(Layer layer, Vector2D location, double radius) {
        super(layer, location, new Vector2D(0, 0), radius, radius);
    }

    @Override
    public Node createView() {
        double radius = width / 2;

        Circle circle = new Circle( radius);

        circle.setCenterX(radius);
        circle.setCenterY(radius);

        circle.setStroke(Color.GREEN);
        circle.setFill(Color.GREEN.deriveColor(1, 1, 1, 0.3));

        return circle;
    }

    @Override
    public void updateVelocity(List<Boid> allBoids, List<Obstacle> allObstacles) {
    }
}
