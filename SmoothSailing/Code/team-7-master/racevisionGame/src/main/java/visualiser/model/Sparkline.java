package visualiser.model;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import shared.model.Leg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Class to process and modify a sparkline display. This display keeps visual
 * track of {@link VisualiserBoat}s in a race and their current
 * placing position as they complete each {@link shared.model.Leg} by
 * passing a course {@link shared.model.Mark}. <br>
 * This sparkline is displayed using the
 * {@link visualiser.Controllers.RaceViewController}.
 */
public class Sparkline {

    /**
     * The race to observe.
     */
    private VisualiserRaceState race;

    /**
     * The boats to observe.
     */
    private ObservableList<VisualiserBoat> boats;

    /**
     * Race legs to observe.
     * We need to observe legs as they may be added after the sparkline is created if race.xml is received after this is created.
     */
    private ObservableList<Leg> legs;


    /**
     * The linchart to plot sparklines on.
     */
    private LineChart<Number, Number> sparklineChart;

    /**
     * The x axis of the sparkline chart.
     */
    private NumberAxis xAxis;

    /**
     * The y axis of the sparkline chart.
     */
    private NumberAxis yAxis;

    /**
     * A map between a boat and its data series in the sparkline.
     * This is used so that we can remove a series when (or if) a boat is removed from the race.
     */
    private Map<VisualiserBoat, XYChart.Series<Number, Number>> boatSeriesMap;




    /**
     * Constructor to set up initial sparkline (LineChart) object
     * @param race The race to listen to.
     * @param sparklineChart JavaFX LineChart for the sparkline.
     */
    public Sparkline(VisualiserRaceState race, LineChart<Number, Number> sparklineChart) {
        this.race = race;
        this.boats = new SortedList<>(race.getBoats());
        this.legs = race.getLegs();

        this.sparklineChart = sparklineChart;
        this.yAxis = (NumberAxis) sparklineChart.getYAxis();
        this.xAxis = (NumberAxis) sparklineChart.getXAxis();

        this.boatSeriesMap = new HashMap<>();

        createSparkline();

    }


    /**
     * Creates and sets initial display for Sparkline for race positions.
     * A data series for each boat in the race is added.
     * Position numbers are displayed.
     */
    private void createSparkline() {

        //We need to dynamically update the sparkline when boats are added/removed.
        boats.addListener((ListChangeListener.Change<? extends VisualiserBoat> c) -> {

            Platform.runLater(() -> {

                while (c.next()) {

                    if (c.wasAdded()) {
                        for (VisualiserBoat boat : c.getAddedSubList()) {
                            addBoatSeries(boat);
                        }

                    } else if (c.wasRemoved()) {
                        for (VisualiserBoat boat : c.getRemoved()) {
                            removeBoatSeries(boat);
                        }
                    }

                }

                //Update height of y axis.
                setYAxisLowerBound();
            });

        });


        legs.addListener((ListChangeListener.Change<? extends Leg> c) -> {
            Platform.runLater(() -> xAxis.setUpperBound(race.getLegCount()));
        });


        //Initialise chart for existing boats.
        for (VisualiserBoat boat : boats) {
            addBoatSeries(boat);
        }


        sparklineChart.setCreateSymbols(false);

        //Set x axis details
        xAxis.setAutoRanging(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setMinorTickVisible(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(race.getLegCount());
        xAxis.setTickUnit(1);


        //The y-axis uses negative values, with the minus sign hidden (e.g., boat in 1st has position -1, which becomes 1, boat in 6th has position -6, which becomes 6).
        //This is necessary to actually get the y-axis labelled correctly. Negative tick count doesn't work.
        //Set y axis details
        yAxis.setAutoRanging(false);

        yAxis.setTickUnit(1);
        yAxis.setMinorTickCount(0);

        yAxis.setUpperBound(0);
        setYAxisLowerBound();

        yAxis.setLabel("Position in Race");

        yAxis.setTickMarkVisible(true);
        yAxis.setTickLabelsVisible(true);
        yAxis.setMinorTickVisible(false);



        //Hide minus number from displaying on axis.
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
            @Override
            public String toString(Number value) {
                if ((value.intValue() == 0) || (value.intValue() < -boats.size())) {
                    return "";

                } else {
                    return String.format("%d", -value.intValue());
                }
            }
        });

    }


    /**
     * Sets the lower bound of the y-axis.
     */
    private void setYAxisLowerBound() {
        yAxis.setLowerBound( -(boats.size() + 1));
    }



    /**
     * Removes the data series for a given boat from the sparkline.
     * @param boat Boat to remove series for.
     */
    private void removeBoatSeries(VisualiserBoat boat) {
        sparklineChart.getData().remove(boatSeriesMap.get(boat));
        boatSeriesMap.remove(boat);
    }


    /**
     * Creates a data series for a boat, and adds it to the sparkline.
     * @param boat Boat to add series for.
     */
    private void addBoatSeries(VisualiserBoat boat) {

        //Create data series for boat.
        XYChart.Series<Number, Number> series = new XYChart.Series<>();


        //All boats start in "last" place.
        series.getData().add(new XYChart.Data<>(0, -(boats.size())));

        //Listen for changes in the boat's leg - we only update the graph when it changes leg.
        boat.legProperty().addListener(
                (observable, oldValue, newValue) -> {

                    //Get the data to plot.
                    List<VisualiserBoat> boatOrder = race.getLegCompletionOrder().get(oldValue);
                    //Find boat position in list.
                    int boatPosition = -(boatOrder.indexOf(boat) + 1);

                    //Get leg number.
                    int legNumber = oldValue.getLegNumber() + 1;


                    //Create new data point for boat's position at the new leg.
                    XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(legNumber, boatPosition);

                    //Add to series.
                    Platform.runLater(() -> series.getData().add(dataPoint));


                });


        //Add to chart.
        sparklineChart.getData().add(series);

        //Color using boat's color. We need to do this after adding the series to a chart, otherwise we get null pointer exceptions.
        series.getNode().setStyle("-fx-stroke: " + colourToHex(boat.getColor()) + ";");


        boatSeriesMap.put(boat, series);

    }


    /**
     * Converts a color to a hex string, starting with a {@literal #} symbol.
     * @param color The color to convert.
     * @return Hex string of the color (e.g., {@literal "#11AB4C"}).
     */
    private static String colourToHex(Color color) {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }

}
