package comp127.weather.widgets;

import comp127.weather.api.CurrentConditions;
import comp127.weather.api.WeatherData;
import edu.macalester.graphics.*;

/**
 * A widget that displays the pressure and humidity of current condition.
 *
 * @author Created by Lucy Tran on 10/24/19.
 */

public class PressureHumidityWidget implements WeatherWidget{
    private final double size;
    private GraphicsGroup group;
    private GraphicsText labelPressure;
    private GraphicsText labelHumidity;
    private Image pressureIcon;
    private Image humidityIcon;
    private GraphicsText description1;
    private GraphicsText description2;

    /**
     * Creates a widget of dimensions size x size.
     */
    public PressureHumidityWidget(double size) {
        this.size = size;

        group = new GraphicsGroup();
        pressureIcon = new Image(0, 0);
        pressureIcon.setMaxWidth(size * 0.5);
        pressureIcon.setMaxHeight(size * 0.5);
        group.add(pressureIcon);

        humidityIcon = new Image(0, 0);
        humidityIcon.setMaxWidth(size * 0.5);
        humidityIcon.setMaxHeight(size * 0.5);
        group.add(humidityIcon);

        labelPressure = new GraphicsText();
        labelPressure.setFont(FontStyle.BOLD, size * 0.1);
        group.add(labelPressure);

        labelHumidity = new GraphicsText();
        labelHumidity.setFont(FontStyle.BOLD, size * 0.1);
        group.add(labelHumidity);

        description1 = new GraphicsText();
        description1.setFont(FontStyle.PLAIN, size * 0.05);
        group.add(description1);

        description2 = new GraphicsText();
        description2.setFont(FontStyle.PLAIN, size * 0.05);
        group.add(description2);

        updateLayout();
    }
    @Override
    public GraphicsObject getGraphics() {
        return group;
    }

    @Override
    public void update(WeatherData data) {
        CurrentConditions currentConditions = data.getCurrentConditions();

        pressureIcon.setImagePath("condition-icons/pressure.png");
        FormattingHelpers format = new FormattingHelpers();
        labelPressure.setText(format.nullSafeHelper(currentConditions.getPressure()) + "Hg");
        description1.setText("Current pressure");

        humidityIcon.setImagePath("condition-icons/humidity.png");
        labelHumidity.setText(format.nullSafeHelper(currentConditions.getHumidity()) + "%");
        description2.setText("Current humidity");

        updateLayout();
    }

    /**
     * Sets the graphic objects to the right position on the widget's UI.
     */
    private void updateLayout() {
        pressureIcon.setCenter(size * 0.25, size * 0.4);
        labelPressure.setCenter(size * 0.25, size * 0.75);
        description1.setCenter(size * 0.25, size * 0.85);

        humidityIcon.setCenter(size * 0.75, size * 0.4);
        labelHumidity.setCenter(size * 0.75, size * 0.75);
        description2.setCenter(size * 0.75, size * 0.85);
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
