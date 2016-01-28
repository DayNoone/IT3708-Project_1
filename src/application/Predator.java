package application;

import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

public class Predator extends Boid {
    public Predator(Layer layer, Vector2D location, double width, double height) {
        super(layer, location, new Vector2D(0, 0), width, height);
    }

    @Override
    public Node createView() {
        return Utils.createPredatorImageView((int) width);
    }

    @Override
    public void updateVelocity(List<Boid> allBoids, List<Obstacle> allObstacles) {
        Boid neighbor = this.findNearestNeighbour(allBoids);
        if (neighbor != null) {
            velocity.add(Vector2D.subtract(neighbor.location, this.location));
        }

        super.avoidObstacles(allObstacles);

        velocity.normalize();
        velocity.multiply(Settings.SPRITE_SPEED);
    }

    private Boid findNearestNeighbour(List<Boid> allBoids) {
        Boid neighbour = allBoids.get(0);
        double distance = (Vector2D.subtract(neighbour.location, this.location)).magnitude();
        for (Boid boid : allBoids) {
            double newDistance = (Vector2D.subtract(boid.location, this.location)).magnitude();
            if (newDistance < distance) {
                distance = newDistance;
                neighbour = boid;
            }
        }
        return neighbour;
    }
}
