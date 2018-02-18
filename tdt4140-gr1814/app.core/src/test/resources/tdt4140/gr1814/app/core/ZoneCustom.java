import com.sun.javafx.scene.paint.GradientUtils.Point;

#CUSTOM ZONE CONSISTING OF USER DEFINES POINTS
#ENSURE DATA POINTS FOLLOW CLOCKWISE ORDER (RIGHT TO LEFT) TO ENSURE NO CROSSOVERS


public class ZoneCustom {
private Point[] pointArr;

public void extractPoints(Point ...args) {
	for (arg in args) {
		pointArr += arg;
	}
}

}