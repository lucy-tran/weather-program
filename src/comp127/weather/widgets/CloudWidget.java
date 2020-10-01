package comp127.weather.widgets;

import comp127.weather.api.ForecastConditions;
import comp127.weather.api.WeatherData;
import edu.macalester.graphics.*;
import edu.macalester.graphics.Image;
import edu.macalester.graphics.Point;

import java.util.List;

/**
 * A widget that displays the forecasting cloud coverage by images and a string.
 *
 * @author Created by Lucy Tran on 10/27/19.
 */

public class CloudWidget implements WeatherWidget {
    private final double size;
    private GraphicsGroup group;

    private Image cloudImage;
    private Image previousIcon;
    private Image nextIcon;
    private GraphicsText predictionDate;
    private GraphicsText predictionTime;
    private GraphicsText cloudCoverage;
    private List<ForecastConditions> forecasts;
    private ForecastConditions selectedForecast;

    /**
     * Creates a cloud coverage widget of dimensions size x size.
     */
    public CloudWidget(double size) {
        this.size = size;
        group = new GraphicsGroup();

        predictionDate = new GraphicsText();
        predictionDate.setFont(FontStyle.BOLD, size * 0.07);
        group.add(predictionDate);

        predictionTime = new GraphicsText();
        predictionTime.setFont(FontStyle.BOLD, size * 0.07);
        group.add(predictionTime);

        cloudCoverage = new GraphicsText();
        cloudCoverage.setFont(FontStyle.BOLD, size * 0.07);
        group.add(cloudCoverage);

        cloudImage = new Image(0, 0);
        cloudImage.setMaxHeight(size);
        cloudImage.setMaxWidth(size);
        group.add(cloudImage);

        previousIcon = new Image(0, 0);
        previousIcon.setMaxWidth(size * 0.1);
        previousIcon.setMaxHeight(size * 0.2);
        group.add(previousIcon);
        previousIcon.setImagePath("condition-icons/back.png");
        previousIcon.setCenter(size * 0.05, size * 0.5);

        nextIcon = new Image(0, 0);
        nextIcon.setMaxWidth(size * 0.1);
        nextIcon.setMaxHeight(size * 0.2);
        group.add(nextIcon);
        nextIcon.setImagePath("condition-icons/next.png");
        nextIcon.setCenter(size - size * 0.05, size * 0.5);
    }

    @Override
    public GraphicsObject getGraphics() {
        return group;
    }

    @Override
    public void update(WeatherData data) {
        forecasts = data.getForecasts();
        if (forecasts != null && forecasts.size() > 0) {
            selectedForecast = forecasts.get(0);
        }
        updateData();
        updateLayout();
    }

    /**
     * Select the forecast to be shown based on the currently shown forecast and the icon image that is clicked on.
     *
     * @param icon determines which forecast to be shown.
     */
    private void selectForecast(Image icon) {
        for (ForecastConditions f : forecasts) {
            if (f.equals(selectedForecast)) {
                if (icon == previousIcon && forecasts.indexOf(f) != 0) {
                    selectedForecast = forecasts.get(forecasts.indexOf(f) - 1);
                    break;
                }
                if (icon == nextIcon && forecasts.indexOf(f) != forecasts.size() - 1) {
                    selectedForecast = forecasts.get(forecasts.indexOf(f) + 1);
                    break;
                }
            }
        }
        updateData();
        updateLayout();
    }

    /**
     * This method updates the data shown on the graphic objects. It updates the
     * image path based on the selected forecast's cloud coverage level.
     */
    public void updateData() {
        FormattingHelpers format = new FormattingHelpers();
        String date = format.dateFormat(selectedForecast.getPredictionTime());
        predictionDate.setText(date);

        String time = format.timeFormat(selectedForecast.getPredictionTime());
        predictionTime.setText(time);

        String cloud = format.nullSafeHelper(selectedForecast.getCloudCoverage());
        cloudCoverage.setText("Cloud coverage: " + cloud);

        String imagePath = "condition-icons/unknown.png";
        Double cloudCoverage = selectedForecast.getCloudCoverage();
        if (cloudCoverage != null) {
            // Cloud coverage is classified based on personal observation of cloudCoverage and weatherDescription.
            // Because weatherDescription is not always about cloud coverage type, we need to specifically sort them by values.
            if (0 <= cloudCoverage && cloudCoverage <= 10) {
                imagePath = "condition-icons/clear-sky.jpg";
            }
            if (10 < cloudCoverage && cloudCoverage <= 25) {
                imagePath = "condition-icons/few-clouds.jpg";
            }
            if (25 < cloudCoverage && cloudCoverage <= 50) {
                imagePath = "condition-icons/scattered-clouds.jpg";
            }
            if (50 < cloudCoverage && cloudCoverage <= 85) {
                imagePath = "condition-icons/broken-clouds.jpg";
            }
            if (85 < cloudCoverage && cloudCoverage <= 100) {
                imagePath = "condition-icons/overcast-clouds.jpg";
            }
        }
        cloudImage.setImagePath(imagePath);
    }

    /**
     * Sets the graphic objects to the right position on the widget's UI.
     */
    public void updateLayout() {
        cloudImage.setCenter(size * 0.5, size * 0.5);
        predictionDate.setCenter(size * 0.25, size * 0.08);
        predictionTime.setCenter(size * 0.78, size * 0.08);
        cloudCoverage.setCenter(size * 0.5, size * 0.9);
    }

    /**
     * Given a position in the widget, this returns the image at that position if one exists
     *
     * @param location pos to check
     * @return null if location is not over an image
     */
    private Image getIconAt(Point location) {
        GraphicsObject obj = group.getElementAt(location);
        if (obj instanceof Image) {
            return (Image) obj;
        }
        return null;
    }

    @Override
    public void onHover(Point position) {
        // This widget does not interact with hovering mouse.
    }

    /**
     * Updates the currently displayed forecast information as the mouse clicks on the widget.
     * If there is not a previous/next icon image at that position, the display does not change.
     */
    @Override
    public void onClick(Point position) {
        Image icon = getIconAt(position);
        if (icon.equals(previousIcon) || icon.equals(nextIcon)) {
            selectForecast(icon);
        }
    }
}

