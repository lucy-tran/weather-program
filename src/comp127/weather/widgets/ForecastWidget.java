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
 * A widget that displays the forecast conditions including temperature, minimum and maximum temperature in 3 hours gap.
 *
 * @author Created by Lucy Tran on 10/25/19.
 * Acknowledgement: Hints were given by COMP 127 professors in the original code.
 */

public class ForecastWidget implements WeatherWidget {
    private final double size;
    private GraphicsGroup group;

    private GraphicsText forecastTemperature;
    private GraphicsText minMaxTemperature;
    private GraphicsText description;
    private GraphicsText predictionDate;
    private GraphicsText predictionTime;
    private Image icon;
    private static String DEGREE = "\u2109";

    private GraphicsGroup boxGroup;  // Holds all the ForecastBox objects

    private List<ForecastBox> boxes = new ArrayList<>();

    /**
     * Creates a forecast widget of dimensions size x size.
     */
    public ForecastWidget(double size) {
        this.size = size;

        group = new GraphicsGroup();
        predictionDate = new GraphicsText();
        predictionDate.setFont(FontStyle.BOLD, size * 0.07);
        group.add(predictionDate);

        predictionTime = new GraphicsText();
        predictionTime.setFont(FontStyle.BOLD, size * 0.07);
        group.add(predictionTime);

        icon = new Image(0, 0);
        icon.setMaxWidth(size);
        icon.setMaxHeight(size * 0.2);
        group.add(icon);

        forecastTemperature = new GraphicsText();
        forecastTemperature.setFont(FontStyle.BOLD, size * 0.07);
        group.add(forecastTemperature);

        minMaxTemperature = new GraphicsText();
        minMaxTemperature.setFont(FontStyle.PLAIN, size * 0.07);
        minMaxTemperature.setFillColor(Color.GRAY);
        group.add(minMaxTemperature);

        description = new GraphicsText();
        description.setFont(FontStyle.PLAIN, size * 0.06);
        group.add(description);

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
        boxGroup.removeAll(); // Remove all the old ForecastBoxes from our list
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

        selectForecast(boxes.get(0));
    }

    /**
     * Sets active the box in parameter, and call other methods to update the widget with the forecast represented by that box.
     *
     * @param box represents the forecast conditions to be shown
     */
    private void selectForecast(ForecastBox box) {
        for (ForecastBox fbox : boxes) {
            boolean active = fbox == box;
            fbox.setActive(active);
        }
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
        String date = FormattingHelpers.dateFormat(forecast.getPredictionTime());
        predictionDate.setText(date);

        String time = FormattingHelpers.timeFormat(forecast.getPredictionTime());
        predictionTime.setText(time);

        icon.setImagePath(forecast.getWeatherIcon());

        forecastTemperature.setText(FormattingHelpers.nullSafeHelper(forecast.getTemperature()) + DEGREE);

        String minTemperature = FormattingHelpers.nullSafeHelper(forecast.getMinTemperature());
        String maxTemperature = FormattingHelpers.nullSafeHelper(forecast.getMaxTemperature());
        minMaxTemperature.setText(minTemperature + DEGREE + " | " + maxTemperature + DEGREE);

        description.setText(forecast.getWeatherDescription());
    }

    /**
     * Sets the graphic objects to the right position on the widget's UI.
     */
    private void updateLayout() {
        predictionDate.setCenter(size * 0.25, size * 0.08);
        predictionTime.setCenter(size * 0.78, size * 0.08);
        icon.setCenter(size * 0.5, size * 0.3);
        forecastTemperature.setCenter(size * 0.5, size * 0.5);
        minMaxTemperature.setCenter(size * 0.5, size * 0.61);
        description.setCenter(size * 0.5, size * 0.7);
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
