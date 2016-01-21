package application;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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
     * @param id
     * @param size The width. The height is calculated as width / 2.0.
     * @return
     */
    public static ImageView createArrowImageView(int id, double size) {

        return createArrowImageView(id, size, size / 2.0, Color.BLUE, Color.BLUE.deriveColor(1, 1, 1, 0.3), 1);

    }

    /**
     * Create an imageview of a right facing arrow.
     * @param width
     * @param height
     * @return
     */
    public static ImageView createArrowImageView(int id, double width, double height, Paint stroke, Paint fill, double strokeWidth) {

        return new ImageView( createArrowImage(id, width, height, stroke, fill, strokeWidth));

    }

    /**
     * Create an image of a right facing arrow.
     * @param width
     * @param height
     * @return
     */
    public static Image createArrowImage(int id, double width, double height, Paint stroke, Paint fill, double strokeWidth) {

        WritableImage wi;

        double arrowWidth = width - strokeWidth * 2;
        double arrowHeight = height - strokeWidth * 2;

        Text text = new Text(String.valueOf(id));
        //text.opacityProperty(0.1);
        text.setFont(new Font(5));
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
        WritableImage a = arrow.snapshot(parameters, wi);
        //text.snapshot(textParameters, a);

        return a;

    }

}