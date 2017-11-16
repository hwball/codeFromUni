package visualiser.model;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import shared.dataInput.RaceDataSource;
import shared.enums.RoundingType;
import shared.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This JavaFX Canvas is used to update and display details for a
 * {@link RaceMap} via the
 * {@link visualiser.Controllers.RaceViewController}.<br>
 * It fills it's parent and cannot be downsized. <br>
 * Details displayed include:
 *  {@link VisualiserBoat}s (and their
 *  {@link TrackPoint}s),
 *  {@link shared.model.Mark}s, a
 *  {@link RaceClock}, a wind direction arrow and
 *  various user selected {@link Annotations}.
 */
public class ResizableRaceCanvas extends ResizableCanvas {

    /**
     * The RaceMap used for converting GPSCoordinates to GraphCoordinates.
     */
    private RaceMap map;

    private Image sailsRight = new Image("/images/sailsRight.png");
    private Image sailsLeft = new Image("/images/sailsLeft.png");
    private Image sailsLuff = new Image("/images/sailsLuff.gif", 25, 10, false, false);

    /**
     * The race state we read data from and draw.
     */
    private VisualiserRaceState raceState;


    private boolean annoName = false;
    private boolean annoAbbrev = true;
    private boolean annoSpeed = false;
    private boolean annoPath = true;
    private boolean annoEstTime = false;
    private boolean annoTimeSinceLastMark = false;
    private boolean annoGuideLine = true;

    private boolean isFullScreen = false;



    /**
     * Constructs a {@link ResizableRaceCanvas} using a given {@link VisualiserRaceEvent}.
     * @param raceState The race state to be drawn.
     */
    public ResizableRaceCanvas(VisualiserRaceState raceState) {
        super();

        this.raceState = raceState;

        RaceDataSource raceData = raceState.getRaceDataSource();

        double lat1 = raceData.getMapTopLeft().getLatitude();
        double long1 = raceData.getMapTopLeft().getLongitude();
        double lat2 = raceData.getMapBottomRight().getLatitude();
        double long2 = raceData.getMapBottomRight().getLongitude();

        this.map = new RaceMap(
                lat1, long1, lat2, long2,
                (int) getWidth(), (int) getHeight() );


    }



    /**
     * Toggle name display in annotation
     */
    public void toggleAnnoName() {
        annoName = !annoName;
    }

    /**
     * Toggle boat path display in annotation
     */
    public void toggleBoatPath() {
        annoPath = !annoPath;
    }

    public void toggleAnnoEstTime() {
        annoEstTime = !annoEstTime;
    }

    /**
     * Toggle boat time display in annotation
     */
    public void toggleAnnoTime() {
        annoTimeSinceLastMark = !annoTimeSinceLastMark;
    }

    /**
     * Toggle abbreviation display in annotation
     */
    public void toggleAnnoAbbrev() {
        annoAbbrev = !annoAbbrev;
    }

    /**
     * Toggle speed display in annotation
     */
    public void toggleAnnoSpeed() {
        annoSpeed = !annoSpeed;
    }

    /**
     * Toggle the guideline annotation
     */
    public void toggleGuideLine() {
        annoGuideLine = !annoGuideLine;
    }




    /**
     * Rotates things on the canvas Note: this must be called in between gc.save() and gc.restore() else they will rotate everything
     *
     * @param degrees Bearing degrees to rotate.
     * @param px    Pivot point x of rotation.
     * @param py    Pivot point y of rotation.
     */
    private void rotate(double degrees, double px, double py) {
        Rotate r = new Rotate(degrees, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }



    /**
     * Draws a circle with a given diameter, centred on a given graph coordinate.
     * @param center The center coordinate of the circle.
     * @param diameter The diameter of the circle.
     * @param paint The paint to use for the circle.
     */
    private void drawCircle(GraphCoordinate center, double diameter, Paint paint) {

        gc.save();

        gc.setFill(paint);
        gc.setStroke(paint);

        //The graphCoordinates are for the center of the point, so we offset them to get the corner coordinate.
        gc.fillOval(
                center.getX() - (diameter / 2),
                center.getY() - (diameter / 2),
                diameter, diameter    );

        gc.restore();
    }

    /**
     * Displays a line on the map with rectangles on the starting and ending point of the line.
     *
     * @param graphCoordinateA Starting Point of the line in GraphCoordinate.
     * @param graphCoordinateB End Point of the line in GraphCoordinate.
     * @param paint            Colour the line is to coloured.
     * @param lineWidth The width of the line.
     */
    private void drawLine(GraphCoordinate graphCoordinateA, GraphCoordinate graphCoordinateB, Paint paint, double lineWidth) {

        gc.save();

        gc.setStroke(paint);
        gc.setFill(paint);

        gc.setLineWidth(lineWidth);

        //Draw line between them.
        gc.strokeLine(
                graphCoordinateA.getX(),
                graphCoordinateA.getY(),
                graphCoordinateB.getX(),
                graphCoordinateB.getY() );

        gc.restore();

    }



    /**
     * Display given name and speed of boat at a graph coordinate
     *
     * @param name       name of the boat
     * @param abbrev     abbreviation of the boat name
     * @param speed      speed of the boat
     * @param coordinate coordinate the text appears
     * @param timeToNextMark The time until the boat reaches the next mark.
     * @param timeSinceLastMark The time since the boat passed the last mark.
     * @param textPaint The color of the text.
     * @param fontSize The size of the font.
     */
    private void drawText(String name, String abbrev, double speed, GraphCoordinate coordinate, String timeToNextMark, String timeSinceLastMark, Paint textPaint, double fontSize) {

        //The text to draw. Built during the function.
        String text = "";


        //Draw name if annotation is enabled.
        if (annoName) {
            text += String.format("%s ", name);
        }

        //Draw abbreviation/country if annotation is enabled.
        if (annoAbbrev) {
            text += String.format("%s ", abbrev);
        }

        //Draw speed if annotation is enabled.
        if (annoSpeed){
            text += String.format("%.2fkn ", speed);
        }

        //Draw time to reach next mark if annotation is enabled.
        if (annoEstTime) {
            text += timeToNextMark;
        }

        //Draw time since last mark if annotation is enabled.
        if(annoTimeSinceLastMark) {
            text += timeSinceLastMark;
        }


        //Offset by 20 pixels horizontally.
        double xCoord = coordinate.getX() + 20;
        double yCoord = coordinate.getY();

        //If the text would extend out of the canvas (to the right), move it left.
        if (xCoord + (text.length() * 7) >= getWidth()) {
            xCoord -= text.length() * 7;
        }


        if (yCoord - (text.length() * 2) <= 0) {
            yCoord += 30;
        }

        gc.save();

        gc.setStroke(textPaint);
        gc.setFill(textPaint);
        gc.setFont(new Font(gc.getFont().getName(), fontSize));

        //Draw text.
        gc.fillText(text, xCoord, yCoord);

        gc.restore();
    }


    /**
     * Draws the label for a given boat. Includes name, abbreviation, speed, time since mark, and time to next mark.
     * @param boat The boat to draw text for.
     */
    private void drawBoatText(VisualiserBoat boat) {

        drawText(
                boat.getName(),
                boat.getCountry(),
                boat.getCurrentSpeed(),
                this.map.convertGPS(boat.getPosition()),
                boat.getTimeToNextMarkFormatted(raceState.getRaceClock().getCurrentTime()),
                boat.getTimeSinceLastMarkFormatted(raceState.getRaceClock().getCurrentTime()),
                Color.BLACK,
                20  );

    }




    /**
     * Draws all of the boats on the canvas.
     */
    private void drawBoats() {

        List<VisualiserBoat> boats = new ArrayList<>(raceState.getBoats());
        //Sort to ensure we draw boats in consistent order.
//        boats.sort(Comparator.comparingInt(Boat::getSourceID));

        //Current draw order:
        //    track points
        //    wake
        //    boat
        //    text

        //Track points.
        if (isFullScreen){
            for (VisualiserBoat boat : boats) {
                drawTrack(boat);
            }

            //Text.
            for (VisualiserBoat boat : boats) {
                drawBoatText(boat);
            }
        }

        //Wake.
//        for (VisualiserBoat boat : boats) {
//            //Only draw wake if they are currently racing.
//            if (boat.getStatus() == BoatStatusEnum.RACING) {
//                drawWake(boat);
//            }
//        }

        //Boat.
        for (VisualiserBoat boat : boats) {
            drawBoat(boat);
        }
    }

    /**
     * Draws a given boat on the canvas.
     * @param boat The boat to draw.
     */
    private void drawBoat(VisualiserBoat boat) {

        if (boat.isClientBoat()) {
            drawClientBoat(boat);
        }

        //Convert position to graph coordinate.
        GraphCoordinate pos = this.map.convertGPS(boat.getPosition());

        //The x coordinates of each vertex of the boat.
        double[] x = {
                pos.getX() - 6,
                pos.getX(),
                pos.getX() + 6  };

        //The y coordinates of each vertex of the boat.
        double[] y = {
                pos.getY() + 12,
                pos.getY() - 12,
                pos.getY() + 12 };

        //The above shape is essentially a triangle 12px wide, and 24px long.


        gc.save();

        //Draw the boat.
        gc.setFill(boat.getColor());

        rotate(boat.getBearing().degrees(), pos.getX(), pos.getY());
        gc.fillPolygon(x, y, x.length);
        gc.restore();

//        if (boat.getSourceID() == ThisBoat.getInstance().getSourceID()) {
//            drawSails(boat);
//        }
    }

    /**
     * Draws extra decorations to show which boat has been assigned to the client.
     * @param boat The client's boat.
     */
    private void drawClientBoat(VisualiserBoat boat) {

        //Convert position to graph coordinate.
        GraphCoordinate pos = this.map.convertGPS(boat.getPosition());

        //The x coordinates of each vertex of the boat.
        double[] x = {
                pos.getX() - 9,
                pos.getX(),
                pos.getX() + 9  };

        //The y coordinates of each vertex of the boat.
        double[] y = {
                pos.getY() + 15,
                pos.getY() - 15,
                pos.getY() + 15 };

        //The above shape is essentially a triangle 24px wide, and 48 long.


        gc.save();

        //Draw the boat.
        gc.setFill(Color.BLACK);

        rotate(boat.getBearing().degrees(), pos.getX(), pos.getY());
        gc.fillPolygon(x, y, x.length);
        gc.restore();

    }

    /**
     * Draws sails for a given boat on the canvas. Sail position is
     * determined by the boats heading and the current wind direction
     * according to the "points of sail".
     * @param boat boat to display sails for
     */
    private void drawSails(VisualiserBoat boat) {
        GraphCoordinate boatPos =
                this.map.convertGPS(boat.getPosition());
        double xPos = boatPos.getX();       // x pos of sail (on boat)
        double yPos = boatPos.getY() - 6;   // y pos of sail (on boat)
        double boatBearing = boat.getBearing().degrees();
        double windDirection = 0; //visualiserRace.getWindDirection().degrees();
        double sailRotateAngle = 0; // rotation for correct sail display
        Image sailImage = null;
        Boolean rightSail = true;

        // Getting the correct Points of Sail
        if (ThisBoat.getInstance().isSailsOut()){
            // correct sail and sailRotateAngle start depending on wind+bearing
            if ((windDirection + 180) > 360) {
                if ((boatBearing < windDirection) &&
                        (boatBearing > windDirection - 180)) {
                    rightSail = false;
                } else {
                    if (boatBearing < 180) {
                        sailRotateAngle = -180;
                    }
                }
            } else {
                if (!((boatBearing > windDirection) &&
                        (boatBearing < windDirection + 180))) {
                    rightSail = false;
                    if (boatBearing > 180) {
                        sailRotateAngle = -180;
                    }
                }
            }

            if (rightSail) {
                sailImage = sailsRight;
                xPos -= 1;  // right align sail to boat edge on canvas
            } else {
                sailImage = sailsLeft;
                xPos -= 5;  // left align sail to boat edge on canvas
            }
            sailRotateAngle += ((boatBearing + windDirection) * 0.5);
        }
        // Sails in = luffing sail
        else {
            xPos -= 6;
            yPos += 1;
            sailImage = sailsLuff;
            sailRotateAngle = boatBearing + 90;
        }

        gc.save();

        // rotate sails based on boats current heading
        rotate(sailRotateAngle, boatPos.getX(), boatPos.getY());
        gc.drawImage(sailImage, xPos, yPos);

        gc.restore();
    }

    /**
     * Draws the wake for a given boat.
     * @param boat Boat to draw wake for.
     */
    private void drawWake(VisualiserBoat boat) {

        //Calculate either end of wake line.
        GraphCoordinate wakeFrom = this.map.convertGPS(boat.getPosition());
        GraphCoordinate wakeTo = this.map.convertGPS(boat.getWake());

        double lineWidth = 4;
        double endPointDiameter = 12;

        //Line.
        drawLine(wakeFrom, wakeTo, Color.DARKBLUE, lineWidth);

        //Draw end-point.
        //drawCircle(wakeTo, endPointDiameter, Color.BLACK);


    }


    private void drawRoundingLines() {


        //ugly hack
        //rounding lines

        //Boat is within an acceptable distance from the mark.
        VisualiserBoat boat = null;
        for (VisualiserBoat visualiserBoat : new ArrayList<>(raceState.getBoats())) {
            if (visualiserBoat.isClientBoat()) {
                boat = visualiserBoat;
            }
        }
        if (boat == null) {
            return;
        }

        Leg currentLeg = boat.getCurrentLeg();

        MarkRoundingData roundingData = raceState.getMarkRoundingSequence().getRoundingData(currentLeg);



        //To screen coords.
        GraphCoordinate legEnd = map.convertGPS(roundingData.getMarkToRound().getPosition());
        GraphCoordinate round1 = map.convertGPS(roundingData.getRoundCheck1());
        GraphCoordinate round2 = map.convertGPS(roundingData.getRoundCheck2());


        gc.strokeLine(
                legEnd.getX(),
                legEnd.getY(),
                round1.getX(),
                round1.getY()   );

        gc.strokeLine(
                legEnd.getX(),
                legEnd.getY(),
                round2.getX(),
                round2.getY()   );

        drawCircle(round1, 12, Color.ORANGE);
        drawCircle(round2, 12, Color.GREEN);

    }


    /**
     * Draws all of the {@link Mark}s on the canvas.
     */
    private void drawMarks() {

        for (Mark mark : new ArrayList<>(raceState.getMarks())) {
            drawMark(mark);
        }

    }


    /**
     * Draws a given mark on the canvas.
     * @param mark The mark to draw.
     */
    private void drawMark(Mark mark) {

        //Calculate screen position.
        GraphCoordinate mark1 = this.map.convertGPS(mark.getPosition());

        double diameter = 10;

        //Draw.
        drawCircle(mark1, diameter, Color.DARKGREEN);

    }



    /**
     * Draws the Race Map.
     * Called when the canvas is resized.
     */
    public void draw() {

        //Clear canvas.
        clear();

        //Update our RaceMap using new canvas size.
        this.map.setWidth((int) getWidth());
        this.map.setHeight((int) getHeight());

        //Draw the race.
        drawRace();

    }


    /**
     * Clears the canvas.
     */
    private void clear() {
        gc.clearRect(0, 0, getWidth(), getHeight());
    }


    /**
     * Draws the race boundary.
     */
    private void drawBoundary() {

        gc.save();

        //Prepare to draw.
        gc.setLineWidth(1);
        gc.setFill(Color.web("#f1f1d4"));


        //Calculate the screen coordinates of the boundary.
        List<GPSCoordinate> boundary = new ArrayList<>(raceState.getBoundary());
        double[] xpoints = new double[boundary.size()];
        double[] ypoints = new double[boundary.size()];

        //For each boundary coordinate.
        for (int i = 0; i < boundary.size(); i++) {
            //Convert.
            GraphCoordinate coord = map.convertGPS(boundary.get(i));
            //Use.
            xpoints[i] = coord.getX();
            ypoints[i] = coord.getY();
        }

        //Draw the boundary.
        gc.fillPolygon(xpoints, ypoints, xpoints.length);

        gc.restore();

    }

    /**
     * Draws the race.
     * Called once per frame, and on canvas resize.
     */
    public void drawRace() {

        //Update RaceMap with new GPS values of race.
        this.map.setGPSTopLeft(raceState.getRaceDataSource().getMapTopLeft());
        this.map.setGPSBotRight(raceState.getRaceDataSource().getMapBottomRight());


        clear();

        //Race boundary.
        drawBoundary();

        //rounding lines
        if (isFullScreen){
//            drawRoundingLines();
            drawRaceLine();
        }

        //Guiding Line
//        if (annoGuideLine){
//            drawRaceLine();
//        }

        //Boats.
        drawBoats();

        //Marks.
        drawMarks();


    }

    /**
     * draws a transparent line around the course that shows the paths boats must travel
     */
    public void drawRaceLine(){
        List<Leg> legs = raceState.getLegs();
        GPSCoordinate legStartPoint = legs.get(0).getStartCompoundMark().getAverageGPSCoordinate();
        GPSCoordinate nextStartPoint;
        for (int i = 0; i < legs.size() -1; i++) {
            nextStartPoint = drawLineRounding(legs, i, legStartPoint);
            legStartPoint = nextStartPoint;
        }
    }

    /**
     * Draws a line around a course that shows where boats need to go. This method
     * draws the line leg by leg
     * @param legs the legs of a race
     * @param index the index of the current leg to use
     * @param legStartPoint The position the current leg.
     * @return the end point of the current leg that has been drawn
     */
    private GPSCoordinate drawLineRounding(List<Leg> legs, int index, GPSCoordinate legStartPoint){
        GPSCoordinate startDirectionLinePoint;
        GPSCoordinate endDirectionLinePoint;
        Bearing bearingOfDirectionLine;

        GPSCoordinate startNextDirectionLinePoint;
        GPSCoordinate endNextDirectionLinePoint;
        Bearing bearingOfNextDirectionLine;

        //finds the direction of the current leg as a bearing
        startDirectionLinePoint = legStartPoint;
        GPSCoordinate tempEndDirectionLinePoint = legs.get(index).getEndCompoundMark().getMark1Position();

        bearingOfDirectionLine = GPSCoordinate.calculateBearing(startDirectionLinePoint, tempEndDirectionLinePoint);

        //after finding the initial bearing pick the mark used for rounding
        endDirectionLinePoint = legs.get(index).getEndCompoundMark().getMarkForRounding(bearingOfDirectionLine).getPosition();
        bearingOfDirectionLine = GPSCoordinate.calculateBearing(startDirectionLinePoint, endDirectionLinePoint);

        //finds the direction of the next leg as a bearing
        if (index < legs.size() -2){ // not last leg
            startNextDirectionLinePoint = legs.get(index + 1).getStartCompoundMark().getMark1Position();
            endNextDirectionLinePoint = legs.get(index + 1).getEndCompoundMark().getMark1Position();
            bearingOfNextDirectionLine = GPSCoordinate.calculateBearing(startNextDirectionLinePoint, endNextDirectionLinePoint);

            double degreesToAdd;
            //find which side is need to be used
            if (legs.get(index).getEndCompoundMark().getRoundingType() == RoundingType.Port ||
                    legs.get(index).getEndCompoundMark().getRoundingType() == RoundingType.SP){
                degreesToAdd = 90;
            }else{
                degreesToAdd = -90;
            }

            //use the direction line to find a point parallel to it by the mark
            GPSCoordinate pointToStartCurve = GPSCoordinate.calculateNewPosition(endDirectionLinePoint,
                    100, Azimuth.fromDegrees(bearingOfDirectionLine.degrees()+degreesToAdd));

            //use the direction line to find a point to curve too
            GPSCoordinate pointToEndCurve = GPSCoordinate.calculateNewPosition(endDirectionLinePoint,
                    100, Azimuth.fromDegrees(bearingOfNextDirectionLine.degrees()+degreesToAdd));

            //use the curve points to find the two control points for the bezier curve
            GPSCoordinate controlPoint;
            GPSCoordinate controlPoint2;
            Bearing bearingOfCurveLine = GPSCoordinate.calculateBearing(pointToStartCurve, pointToEndCurve);
            if ((bearingOfDirectionLine.degrees() - bearingOfNextDirectionLine.degrees() +360)%360< 145){
                //small turn
                controlPoint = GPSCoordinate.calculateNewPosition(pointToStartCurve,
                        50, Azimuth.fromDegrees(bearingOfCurveLine.degrees()+(degreesToAdd/2)));
                controlPoint2 = controlPoint;
            }else{
                //large turn
                controlPoint = GPSCoordinate.calculateNewPosition(pointToStartCurve,
                        150, Azimuth.fromDegrees(bearingOfCurveLine.degrees()+degreesToAdd));
                controlPoint2 = GPSCoordinate.calculateNewPosition(pointToEndCurve,
                        150, Azimuth.fromDegrees(bearingOfCurveLine.degrees()+degreesToAdd));
            }


            //change all gps into graph coordinate
            GraphCoordinate startPath = this.map.convertGPS(startDirectionLinePoint);
            GraphCoordinate curvePoint = this.map.convertGPS(pointToStartCurve);
            GraphCoordinate curvePointEnd = this.map.convertGPS(pointToEndCurve);
            GraphCoordinate c1 = this.map.convertGPS(controlPoint);
            GraphCoordinate c2 = this.map.convertGPS(controlPoint2);

            gc.save();

            gc.setLineWidth(2);
            gc.setStroke(getLineColor(legs.get(index)));


            gc.beginPath();
            gc.moveTo(startPath.getX(), startPath.getY());
            gc.lineTo(curvePoint.getX(), curvePoint.getY());
            drawArrowHead(startDirectionLinePoint, pointToStartCurve);
            gc.bezierCurveTo(c1.getX(), c1.getY(), c2.getX(), c2.getY(), curvePointEnd.getX(), curvePointEnd.getY());
            gc.stroke();
            gc.closePath();
            //gc.save();
            gc.restore();

            return pointToEndCurve;
        }else{//last leg so no curve
            GraphCoordinate startPath = this.map.convertGPS(legStartPoint);
            GraphCoordinate endPath = this.map.convertGPS(legs.get(index).getEndCompoundMark().getAverageGPSCoordinate());

            gc.save();

            gc.setLineWidth(2);
            gc.setStroke(getLineColor(legs.get(index)));

            gc.beginPath();
            gc.moveTo(startPath.getX(), startPath.getY());
            gc.lineTo(endPath.getX(), endPath.getY());
            gc.stroke();
            gc.closePath();
            //gc.save();
            drawArrowHead(legStartPoint, legs.get(index).getEndCompoundMark().getAverageGPSCoordinate());

            gc.restore();

            return null;
        }
    }

    private Color getLineColor(Leg leg) {
        if(ThisBoat.getInstance().getLegNumber() == leg.getLegNumber()){
            return Color.ORANGE;
        }else{
            return Color.MEDIUMAQUAMARINE;
        }
    }

    private void drawArrowHead(GPSCoordinate start, GPSCoordinate end){

        GraphCoordinate lineStart = this.map.convertGPS(start);
        GraphCoordinate lineEnd = this.map.convertGPS(end);

        double arrowAngle = Math.toRadians(45.0);
        double arrowLength = 10.0;
        double dx = lineStart.getX() - lineEnd.getX();
        double dy = lineStart.getY() - lineEnd.getY();
        double angle = Math.atan2(dy, dx);
        double x1 = Math.cos(angle + arrowAngle) * arrowLength + lineEnd.getX();
        double y1 = Math.sin(angle + arrowAngle) * arrowLength + lineEnd.getY();

        double x2 = Math.cos(angle - arrowAngle) * arrowLength + lineEnd.getX();
        double y2 = Math.sin(angle - arrowAngle) * arrowLength + lineEnd.getY();
        gc.strokeLine(lineEnd.getX(), lineEnd.getY(), x1, y1);
        gc.strokeLine(lineEnd.getX(), lineEnd.getY(), x2, y2);
    }




    /**
     * Draws all track points for a given boat. Colour is set by boat, opacity by track point.
     * This checks if {@link #annoPath} is enabled.
     * @param boat The boat to draw tracks for.
     * @see TrackPoint
     */
    private void drawTrack(VisualiserBoat boat) {
        //Check that track points are enabled.
        if (this.annoPath) {

            List<TrackPoint> trackPoints = new ArrayList<>(boat.getTrack());

            if (trackPoints.size() > 2 ) {

                gc.save();

                gc.setLineWidth(3);


                //Draw a line between each adjacent pair of track points.
                for (int i = 0; i < trackPoints.size() - 1; i++) {

                    //Convert the GPSCoordinate to a screen coordinate.
                    GraphCoordinate scaledCoordinate1 = this.map.convertGPS(trackPoints.get(i).getCoordinate());
                    GraphCoordinate scaledCoordinate2 = this.map.convertGPS(trackPoints.get(i + 1).getCoordinate());

                    double alpha = trackPoints.get(i).getAlpha();
                    Paint fadedPaint = new Color(
                            boat.getColor().getRed(),
                            boat.getColor().getGreen(),
                            boat.getColor().getBlue(),
                            alpha   );

                    //Apply the faded boat color.
                    gc.setFill(fadedPaint);
                    gc.setStroke(fadedPaint);

                    gc.strokeLine(
                            scaledCoordinate1.getX(),
                            scaledCoordinate1.getY(),
                            scaledCoordinate2.getX(),
                            scaledCoordinate2.getY()    );
                }


                gc.restore();

            }
        }

    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }
}
