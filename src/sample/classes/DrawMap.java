package sample.classes;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

public class DrawMap {


    public DrawMap(){

    }


    // draws a map
    public static Canvas drawMap(List<Node> map) {

        Canvas canvas = new Canvas(600, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.RED);

        for (Node node : map) {
            gc.setStroke(Color.BLUE);
            double startX = node.getCoordinates().getX()*20;
            double startY = node.getCoordinates().getY()*20;
            gc.fillOval(startX-3, startY-3, 5, 5);
            gc.strokeText(node.toString(),startX +6, startY+14 );
            for (Edge edge : node.getAdjencencyList()) {
                double endX = edge.getTargetNode().getCoordinates().getX()*20;
                double endY = edge.getTargetNode().getCoordinates().getY()*20;
                drawArrow(gc, startX, startY, endX, endY, Color.BLUE);
            }
        }
        return canvas;
    }



    // draws an arrow
    public static void drawArrow(GraphicsContext gc, double startX, double startY, double endX, double endY, Color color){

        gc.setStroke(color);
        gc.strokeLine(startX, startY, endX, endY);

        //the below code is used to determine coordinates of two points that are used to draw an arrowhead
        final double deltax = startX - endX;
        double result;
        if (deltax == 0.0d) {
            result = 0;
            if(startY != endY){
                result = Math.PI / 2;
            }
        }
        else {
            result = Math.atan((startY - endY) / deltax) + (startX < endX ? Math.PI : 0);
        }

        final double angle = result;

        final double arrowAngle = Math.PI / 14.0d;
        int arrowSize = 15;

        final double x1 = arrowSize * Math.cos(angle - arrowAngle);
        final double y1 = arrowSize * Math.sin(angle - arrowAngle);
        final double x2 = arrowSize * Math.cos(angle + arrowAngle);
        final double y2 = arrowSize * Math.sin(angle + arrowAngle);

        if(endX == startX && endY > startY){
            gc.strokeLine(endX + x1, endY + y1 - 2*arrowSize, endX, endY );
            gc.strokeLine(endX + x2, endY + y2 - 2*arrowSize, endX, endY );
            gc.strokeLine(endX + x1, endY + y1 - 2*arrowSize, endX + x2, endY + y2 - 2*arrowSize);

        }else{
            gc.strokeLine(endX + x1, endY + y1, endX, endY );
            gc.strokeLine(endX + x2, endY + y2, endX, endY );
            gc.strokeLine(endX + x2, endY + y2, endX + x1, endY + y1 );
        }
    }


    // draws the shortesp path in green
    public static void drawShortestPath(Canvas canvas, List<Node> shortestRout){

        GraphicsContext gc = canvas.getGraphicsContext2D();

        for(int i = 0; i < shortestRout.size()-1; i++){
            Node startNode = shortestRout.get(i);
            Node endNode = shortestRout.get(i+1);
            drawArrow(gc,startNode.getCoordinates().getX()*20, startNode.getCoordinates().getY()*20,
                    endNode.getCoordinates().getX()*20, endNode.getCoordinates().getY()*20, Color.LIGHTGREEN);
        }
    }
}
