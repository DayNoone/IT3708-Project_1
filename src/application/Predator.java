package application;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Predator extends Boid {
    public Predator(Layer layer, Vector2D location, double width, double height) {
        super(layer, location, new Vector2D(0, 0), width, height);
    }

    @Override
    public Node createView() {
        return (new ImageView(new Image("res/Predator.png", width, height, true, true)));
    }

    @Override
    public void updateVelocity(List<Boid> allBoids, List<Obstacle> allObstacles, List<Predator> allPredators) {
        Boid neighbor = this.findNearestNeighbor(allBoids);
        List<Predator> neighbours = findNeighbor(allPredators);
        Vector2D newVector = new Vector2D(0, 0);
        if (neighbor != null) {
            Vector2D difference = Vector2D.subtract(neighbor.location, this.location);
            difference.normalize();
            newVector.add(difference);
            }
        velocity.add(newVector);

        Vector2D sep = calculateSeparationForce(neighbours);
        sep.normalize();
        sep.multiply(Settings.PREDATOR_SEPERATION_WEIGHT/10);
        velocity.add(sep);

        super.avoidObstacles(allObstacles);

        velocity.normalize();
        velocity.multiply(Settings.PREDATOR_SPEED);
    }

    private List<Predator> findNeighbor(List<Predator> allPredators) {
        List<Predator> neighbours =  new ArrayList<Predator>();

        allPredators.forEach(targetPredator -> {
            Vector2D desired = Vector2D.subtract(targetPredator.location, this.location);
            double d = desired.magnitude();
            if (d < Settings.NEIGHBOUR_RADIUS) {
                neighbours.add(targetPredator);
            }
        });
        return neighbours;
    }

    private Vector2D calculateSeparationForce(List<Predator> neighbors) {
        // average of the vectors from boid to neighbours
        Vector2D seperation = new Vector2D(0, 0);
        for (Predator neighbour: neighbors) {
            double distance = Vector2D.subtract(this.location, neighbour.location).magnitude();
            if ((0 < distance) && (distance < Settings.NEIGHBOUR_RADIUS )) {
                Vector2D difference = Vector2D.subtract(this.location, neighbour.location);
                difference.normalize();
                difference.div(distance);
                seperation.add(difference);
            }
        }
        if (neighbors.size() > 0) {
            seperation.div(neighbors.size());
        }
        return seperation;
    }

    private Boid findNearestNeighbor(List<Boid> allBoids) {
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
