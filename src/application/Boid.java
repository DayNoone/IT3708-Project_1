package application;

import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class Boid extends Sprite {

    public Boid(Layer layer, Vector2D location, Vector2D velocity, double width, double height) {
        super(layer, location, velocity, width, height);
    }

    @Override
    public Node createView() {
        return Utils.createArrowImageView((int) width);
    }

    @Override
    public void updateVelocity(List<Boid> allBoids, List<Obstacle> allObstacles, List<Predator> allPredators) {
        List<Boid> neighbors = this.findNeighbours(allBoids);
        if (neighbors.size() != 0) {
            Vector2D sep = calculateSeparationForce(neighbors);
            Vector2D align = calculateAlignmentForce(neighbors);
            Vector2D coh = calculateCohesionForce(neighbors);

            sep.normalize();
            align.normalize();
            coh.normalize();

            sep.multiply(Settings.SEPERATION_WEIGHT);
            align.multiply(Settings.ALIGNMENT_WEIGHT);
            coh.multiply(Settings.COHESION_WEIGHT);

            velocity.add(sep);
            velocity.add(align);
            velocity.add(coh);
        }

        escapePredator(allPredators);

        super.avoidObstacles(allObstacles);

        velocity.normalize();
        velocity.multiply(Settings.SPRITE_SPEED);
    }

    private void escapePredator(List<Predator> allPredators) {
        Vector2D newVelocity = new Vector2D(0, 0);
        for (Predator predator: allPredators) {
            Vector2D difference = Vector2D.subtract(this.location, predator.location);
            double distance = difference.magnitude();
            if (distance < Settings.NEIGHBOUR_RADIUS+50) {
                difference.normalize();
                difference.div(distance);
                newVelocity.add(difference);
            }
        }
        newVelocity.normalize();
        velocity.add(newVelocity);
    }

    private Vector2D calculateSeparationForce(List<Boid> neighbors) {
        // average of the vectors from boid to neighbours
        Vector2D seperation = new Vector2D(0, 0);
        int counter = 0;
        for (Boid neighbour: neighbors) {
            double distance = Vector2D.subtract(this.location, neighbour.location).magnitude();
            if ((0 < distance) && (distance < (Settings.NEIGHBOUR_RADIUS / 2))) {
                Vector2D difference = Vector2D.subtract(this.location, neighbour.location);
                difference.normalize();
                difference.div(distance);
                seperation.add(difference);
                counter++;
            }
        }
        if (counter > 0) {
            seperation.div(counter);
        }
        return seperation;
    }

    private Vector2D calculateAlignmentForce(List<Boid> neighbors) {
        // average of neighbours Vectors
        Vector2D alignment = new Vector2D(0, 0);
        neighbors.forEach(neighbour -> {
            alignment.add(neighbour.velocity);
        });

        alignment.div(neighbors.size());
        return alignment;
    }

    private Vector2D calculateCohesionForce(List<Boid> neighbors) {
        // vector of neightbours positions
        Vector2D cohesion = new Vector2D(0, 0);
        neighbors.forEach(neighbour -> {
            cohesion.add(neighbour.location);
        });
        cohesion.div(neighbors.size());
        return Vector2D.subtract(cohesion, this.location);
    }

    public List<Boid> findNeighbours(List<Boid> allBoids) {
        List<Boid> neighbours =  new ArrayList<Boid>();

        allBoids.forEach(targetBoid -> {
            Vector2D desired = Vector2D.subtract(targetBoid.location, this.location);
            double d = desired.magnitude();
            if (d < Settings.NEIGHBOUR_RADIUS) {
                neighbours.add(targetBoid);
            }
        });
        return neighbours;
    }
}