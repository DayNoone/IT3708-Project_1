package application;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class Utils {

    public static double map(double value, double currentRangeStart, double currentRangeStop, double targetRangeStart, double targetRangeStop) {
        return targetRangeStart + (targetRangeStop - targetRangeStart) * ((value - currentRangeStart) / (currentRangeStop - currentRangeStart));
    }

    /**
     * Create an imageview of a right facing arrow.
     * @param size The width. The height is calculated as width / 2.0.
     * @return
     */
    public static ImageView createArrowImageView(double size) {
        return (new ImageView(new Image("res/Boid.png", size, size/2.0, true, true)));
        //return createArrowImageView(id, size, size / 2.0, Color.BLUE, Color.BLUE.deriveColor(1, 1, 1, 0.3), 1);

    }
    public static ImageView createPredatorImageView(double size) {
        return createArrowImageView(size, size / 2.0, Color.BLUE, Color.BLUE.deriveColor(1, 1, 1, 0.3), 1);

    }


    public static ImageView createObstacleImageView(double radius) {
        //return (new ImageView(new Image("res/Boid.png", size, size/2.0, true, true)));
        return (new ImageView(createObstacleImage(radius)));
    }

    public static Image createObstacleImage(double radius) {
        WritableImage wi = new WritableImage((int) radius*2, (int) radius*2);
        Circle circle = new Circle();
        circle.setRadius((float) radius);
        circle.setFill(Color.BLACK);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);

        circle.snapshot(parameters, wi);
        return wi;
    }

    /**
     * Create an imageview of a right facing arrow.
     * @param width
     * @param height
     * @return
     */
    public static ImageView createArrowImageView(double width, double height, Paint stroke, Paint fill, double strokeWidth) {
        return new ImageView( createArrowImage(width, height, stroke, fill, strokeWidth));
    }

    /**
     * Create an image of a right facing arrow.
     * @param width
     * @param height
     * @return
     */
    public static Image createArrowImage(double width, double height, Paint stroke, Paint fill, double strokeWidth) {

        WritableImage wi;

        double arrowWidth = width - strokeWidth * 2;
        double arrowHeight = height - strokeWidth * 2;

        SnapshotParameters textParameters = new SnapshotParameters();
        textParameters.setFill(Color.TRANSPARENT);


        Polygon arrow = new Polygon( 0, 0, arrowWidth, arrowHeight / 2, 0, arrowHeight); // left/right lines of the arrow
        arrow.setStrokeLineJoin(StrokeLineJoin.MITER);
        arrow.setStrokeLineCap(StrokeLineCap.SQUARE);
        arrow.setStroke(stroke);
        arrow.setFill(fill);
        arrow.setStrokeWidth(strokeWidth);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);

        int imageWidth = (int) width;
        int imageHeight = (int) height;

        wi = new WritableImage( imageWidth, imageHeight);
        arrow.snapshot(parameters, wi);

        return wi;

    }

}