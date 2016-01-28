package application;

import javafx.scene.Node;

import java.nio.channels.SeekableByteChannel;
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
    public void updateVelocity(List<Boid> allBoids, List<Obstacle> allObstacles, List<Predator> allPredators) {
        Boid neighbor = this.findNearestNeighbour(allBoids);
        List<Predator> neighbours = findNeighbour(allPredators);
        Vector2D newVector = new Vector2D(0, 0);
        if (neighbor != null) {
            Vector2D difference = Vector2D.subtract(neighbor.location, this.location);
            difference.normalize();
            newVector.add(difference);
            }
        velocity.add(newVector);

        Vector2D sep = calculateSeparationForce(neighbours);
        sep.normalize();
        sep.multiply(Settings.PREDATOR_SEPERATION_WEIGHT);
        velocity.add(sep);

        super.avoidObstacles(allObstacles);

        velocity.normalize();
        velocity.multiply(Settings.PREDATOR_SPEED);
    }

    private List<Predator> findNeighbour(List<Predator> allPredators) {
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
        int counter = 0;
        for (Predator neighbour: neighbors) {
            double distance = Vector2D.subtract(this.location, neighbour.location).magnitude();
            if ((0 < distance) && (distance < Settings.NEIGHBOUR_RADIUS )) {
                Vector2D difference = Vector2D.subtract(this.location, neighbour.location);
                difference.normalize();
                difference.div(distance);
                seperation.add(difference);
                counter++;
            }
        }
        if (neighbors.size() > 0) {
            seperation.div(neighbors.size());
        }
        return seperation;
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
