package comp127.weather.widgets;

import comp127.weather.api.CurrentConditions;
import comp127.weather.api.WeatherData;
import edu.macalester.graphics.*;

/**
 * A widget that displays the sunrise and sunset time of current condition.
 *
 * @author Created by Lucy Tran on 10/24/19.
 */
public class SunWidget implements WeatherWidget {
    private final double size;
    private GraphicsGroup group;
    private GraphicsText labelSunrise;
    private GraphicsText labelSunset;
    private Image iconSunrise;
    private Image iconSunset;
    private GraphicsText description1;
    private GraphicsText description2;

    /**
     * Creates a widget of dimensions size x size.
     */
    public SunWidget(double size) {
        this.size = size;

        group = new GraphicsGroup();
        iconSunrise = new Image(0, 0);
        iconSunrise.setMaxWidth(size * 0.5);
        iconSunrise.setMaxHeight(size * 0.5);
        group.add(iconSunrise);

        iconSunset = new Image(0, 0);
        iconSunset.setMaxWidth(size * 0.5);
        iconSunset.setMaxHeight(size * 0.5);
        group.add(iconSunset);

        labelSunrise = new GraphicsText();
        labelSunrise.setFont(FontStyle.BOLD, size * 0.1);
        group.add(labelSunrise);

        labelSunset = new GraphicsText();
        labelSunset.setFont(FontStyle.BOLD, size * 0.1);
        group.add(labelSunset);

        description1 = new GraphicsText();
        description1.setFont(FontStyle.PLAIN, size * 0.05);
        group.add(description1);

        description2 = new GraphicsText();
        description2.setFont(FontStyle.PLAIN, size * 0.05);
        group.add(description2);

        updateLayout();
    }

    @Override
    public void update(WeatherData data) {
        CurrentConditions currentConditions = data.getCurrentConditions();

        iconSunrise.setImagePath("condition-icons/sunrise-icon.png");
        FormattingHelpers format = new FormattingHelpers();
        labelSunrise.setText(format.timeFormat(currentConditions.getSunriseTime()));
        description1.setText("Sunrise time");

        iconSunset.setImagePath("condition-icons/sunset-icon.png");
        labelSunset.setText(format.timeFormat(currentConditions.getSunsetTime()));
        description2.setText("Sunset time");

        updateLayout();
    }

    /**
     * Sets the graphic objects to the right position on the widget's UI.
     */
    private void updateLayout() {
        iconSunrise.setCenter(size * 0.25, size * 0.3);
        labelSunrise.setCenter(size * 0.25, size * 0.7);
        description1.setCenter(size * 0.25, size * 0.8);

        iconSunset.setCenter(size * 0.75, size * 0.3);
        labelSunset.setCenter(size * 0.75, size * 0.7);
        description2.setCenter(size * 0.75, size * 0.8);
    }

    @Override
    public GraphicsObject getGraphics() {
        return group;
    }

    @Override
    public void onHover(Point position) {
        // This widget is not interactive, so this method does nothing.
    }

    @Override
    public void onClick(Point position) {
        // This widget is not interactive, so this method does nothing.
    }
}
