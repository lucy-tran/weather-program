package comp127.weather.widgets;

import comp127.weather.api.ForecastConditions;
import comp127.weather.api.WeatherData;
import edu.macalester.graphics.*;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A widget that displays the forecast wind speed and direction, with a compass representing wind direction.
 *
 * @author Created by Lucy Tran on 10/26/19.
 */

public class WindWidget implements WeatherWidget {
    private final double size;
    private GraphicsGroup group;
    private Image compass;
    private Line arrow;
    private GraphicsText windSpeed;
    private GraphicsText windDirectionDescription;
    private Double windDirection;
    private double angleRadians;

    private List<ForecastBox> boxes = new ArrayList<>();
    private GraphicsGroup boxGroup;  // Holds all the ForecastBox objects

    /**
     * Creates a wind forecast widget of dimensions size x size.
     */
    public WindWidget(double size) {
        this.size = size;
        group = new GraphicsGroup();

        compass = new Image(0, 0);
        compass.setImagePath("condition-icons/compass.png");
        compass.setMaxWidth(size);
        compass.setMaxHeight(size * 0.5);
        group.add(compass);

        arrow = new Line(0, 0, 0, 0);
        arrow.setStrokeWidth(size * 0.02);
        arrow.setStrokeColor(Color.DARK_GRAY);
        arrow.setStartPosition(size * 0.51, size * 0.35);
        group.add(arrow);

        windSpeed = new GraphicsText();
        windSpeed.setFont(FontStyle.PLAIN, size * 0.05);
        group.add(windSpeed);

        windDirectionDescription = new GraphicsText();
        windDirectionDescription.setFont(FontStyle.PLAIN, size * 0.05);
        group.add(windDirectionDescription);

        boxGroup = new GraphicsGroup();
        group.add(boxGroup);

        updateLayout();
    }

    @Override
    public GraphicsObject getGraphics() {
        return group;
    }

    @Override
    public void update(WeatherData data) {
        boxGroup.removeAll();
        boxes.clear();
        List<ForecastConditions> forecasts = data.getForecasts();
        double xPosition = size * 0.05;
        double yPosition = 0;
        for (ForecastConditions f : forecasts) {
            if (xPosition + 0.04 * size > size - size * 0.05) {
                yPosition += size * 0.06;
                xPosition = size * 0.05;
            }
            ForecastBox fbox = new ForecastBox(f, xPosition, yPosition, size * 0.03, size * 0.05);
            boxGroup.add(fbox);
            boxes.add(fbox);
            xPosition += size * 0.04;
        }
        updateLayout();
        selectForecast(boxes.get(0));
    }

    /**
     * Sets active the box in parameter, and call other methods to update the widget with the forecast represented by that box.
     *
     * @param box represents the forecast conditions to be shown
     */
    private void selectForecast(ForecastBox box) {
        for (ForecastBox fbox : boxes) {
            fbox.setActive(false);
        }
        box.setActive(true);
        ForecastConditions forecast = box.getForecast();
        updateData(forecast);
        updateLayout();
    }

    /**
     * Updates the data shown on the widget's graphic objects, based on the parameter forecast conditions.
     *
     * @param forecast the forecast conditions to be shown.
     */
    private void updateData(ForecastConditions forecast) {
        FormattingHelpers format = new FormattingHelpers();
        String speed = format.nullSafeHelper(forecast.getWindSpeed());
        windSpeed.setText("Wind speed: " + speed + "mph");

        windDirectionDescription.setText("Wind direction: " + forecast.getWindDirectionAsString());
        windDirection = forecast.getWindDirectionInDegrees();
    }

    /**
     * Sets the graphic objects to the right position on the widget's UI.
     */
    private void updateLayout() {
        compass.setCenter(size * 0.5, size * 0.35);

        double arrowLength = size * 0.12;
        // The end position is set so that the arrow changes clockwise with the degree,
        // starting Northbound with windDirection of 0 degree.
        angleRadians = windDirection != null ? Math.toRadians(windDirection) : Math.toRadians(0);
        double x2 = size * 0.51 + arrowLength * Math.sin(angleRadians);
        double y2 = size * 0.35 + arrowLength * -Math.cos(angleRadians);
        arrow.setEndPosition(x2, y2);

        windSpeed.setCenter(size * 0.5, size * 0.68);
        windDirectionDescription.setCenter(size * 0.5, size * 0.75);
        boxGroup.setCenter(size * 0.5, size * 0.9);
    }

    /**
     * Given a position in the widget, this returns the ForecastBox at that position if one exists
     *
     * @param location pos to check
     * @return null if not over a forecast box
     */
    private ForecastBox getBoxAt(Point location) {
        GraphicsObject obj = group.getElementAt(location);
        if (obj instanceof ForecastBox) {
            return (ForecastBox) obj;
        }
        return null;
    }

    /**
     * Updates the currently displayed forecast information as the mouse moves over the widget.
     * If there is not a ForecastBox at that position, the display does not change.
     */
    @Override
    public void onHover(Point position) {
        ForecastBox selectedBox = getBoxAt(position);
        if (selectedBox != null) {
            selectForecast(selectedBox);
        }
    }

    @Override
    public void onClick(Point position) {
        // This widget does nothing on click in the WeatherProgram.
    }
}
