package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import sample.classes.*;
import java.io.IOException;
import java.util.List;

public class Controller {

    @FXML
    private RadioButton radioStep;
    @FXML
    private Button nextButton;
    @FXML
    private TextArea textArea;
    @FXML
    private GridPane mapCanvas;
    @FXML
    private TextField fileName;


    List<SearchState> states = null;
    List<Node> startingMap = null;
    List <Node> map = null;
    Canvas canvas = null;
    List<Node> shortestPath = null;
    boolean colorsExplained = false;



    // logic for "Find Path" button
    public void onButtonClicked(){
        textArea.clear();
        colorsExplained = false;
        nextButton.visibleProperty().setValue(false);
        if(!mapCanvas.getChildren().isEmpty()){
            for(int i =0 ; i < mapCanvas.getChildren().size(); i++){
                Canvas canvas = (Canvas)mapCanvas.getChildren().get(i);
                canvas.getGraphicsContext2D().clearRect(0, 0, 600, 500);
            }
        }

        String fileNameSt = fileName.getText();

        try{
            startingMap = CavernFileReader.readCavernFile(fileNameSt);
        }catch (IOException e){
            // Display a dialog if exception found
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Path finder");
                    alert.setContentText(e.getMessage());
                    alert.getButtonTypes().setAll(ButtonType.NEXT);
                    alert.showAndWait();
                }
            });
            return;
        }

        map = reversedMap(startingMap);

        if(!mapCanvas.getChildren().isEmpty()){
            canvas = (Canvas) mapCanvas.getChildren().get(mapCanvas.getChildren().size()-1);
            GraphicsContext gs = canvas.getGraphicsContext2D();
            gs.clearRect(0, 0, 600, 500);
        }

        mapCanvas.getChildren().remove(canvas);

        if(radioStep.isSelected() == true){
            if(states != null){
                states.clear();
            }
            if(canvas != null){
                canvas.getGraphicsContext2D().clearRect(0, 0, 600, 500);
            }

            nextButton.visibleProperty().setValue(true);
            states = AStarAlgorithm.getSearchStateList(map);
            AStarAlgorithm.computePath(map);
            shortestPath = AStarAlgorithm.shortestPath(map);
            AStarAlgorithm.pathDistance(shortestPath);
            canvas = DrawMap.drawMap(map);
            mapCanvas.getChildren().add(canvas);

        }else {
            if(states != null){
                states.clear();
            }

            AStarAlgorithm.computePath(map);
            List<sample.classes.Node> shortestPath = AStarAlgorithm.shortestPath(map);
            AStarAlgorithm.pathDistance(shortestPath);
            showShortestPath(shortestPath);
            canvas = DrawMap.drawMap(map);
            DrawMap.drawShortestPath(canvas, shortestPath);
            mapCanvas.getChildren().add(canvas);
        }
    }




    // logic for "Next" button used when expanding nodes manually
    public void onNextButtonClicked(){

        String message = "";
        canvas = DrawMap.drawMap(map);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.getGraphicsContext2D().clearRect(0, 0, 600, 500);

        if(states.size() > 0){

            mapCanvas.getChildren().add(canvas);

            try{
                Node currentNode = states.get(0).getCurrentLocation();
                gc.setFill(Color.LIGHTGREEN);
                gc.fillOval((currentNode.getCoordinates().getX()*20)-6,
                        (currentNode.getCoordinates().getY()*20)-6, 10, 10 );
                message += "Expanding node: " + currentNode.toString() + "\n";
            }catch (NullPointerException e){
                //do nothing
            }

            if(states.size() != 1){
                message += "Nodes considered for expanding next: \n";
            }

            try{
                for (Node node : states.get(0).getExplored()) {
                    gc.setFill(Color.BLACK);
                    gc.fillOval((node.getCoordinates().getX()*20)-6,
                            (node.getCoordinates().getY()*20)-6, 10, 10);
                }
            }catch (NullPointerException e){
                //do nothing
            }

            if(states.size() == 1){
                DrawMap.drawShortestPath(canvas, shortestPath);
                nextButton.visibleProperty().setValue(false);
                showShortestPath(shortestPath);
                return;
            }

            for (Node node : states.get(0).getFrontiere()) {
                gc.setFill(Color.YELLOW);
                gc.fillOval((node.getCoordinates().getX()*20)-6,
                        (node.getCoordinates().getY()*20)-6, 10, 10);
                if(states.size() != 1){
                    message += node.toString() + ", path cost: " + node.getPathCost() + ", estimated distance to goal: "
                            + node.getEstimatedGoalPath() + ", total function cost: " +node.getTotalDistance() + "\n";
                }
            }

            if(!colorsExplained){
                gc.setFill(Color.YELLOW);
                gc.fillOval(10, 430, 10, 10);
                gc.setStroke(Color.BLACK);
                gc.strokeText("Frontier", 25, 440);
                gc.setFill(Color.LIGHTGREEN);
                gc.fillOval(10, 450, 10, 10);
                gc.setStroke(Color.BLACK);
                gc.strokeText("Current Node", 25, 460);
                gc.setFill(Color.BLACK);
                gc.fillOval(10, 470, 10, 10);
                gc.setStroke(Color.BLACK);
                gc.strokeText("Expanded Nodes", 25, 480);
                colorsExplained = true;
            }

            textArea.setText(message);
            states.remove(0);
        }
    }






    // displays a list of nodes that need to be followed to get to goal by shortest path
    private void showShortestPath(List<Node> shortestPath){
        double totalDistance = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("The shortest path:\n");
        for(int i = 0; i < shortestPath.size()-1; i++){
            double distance = shortestPath.get(i).getCoordinates().distance(shortestPath.get(i+1).getCoordinates());
            sb.append(shortestPath.get(i).getName() + " --> " + shortestPath.get(i+1).getName() +" = "+ distance + "\n");
            totalDistance += distance;
        }
        sb.append("Total distance = " +totalDistance);

        textArea.setText(sb.toString());
    }


    // since the Y coordinates in the canvas work differently the coordinates need to be adjusted so the map is not
    // displayed upside-down
    private List<Node> reversedMap(List<Node> map){
        double minY = Double.MAX_VALUE;
        double maxY = 0;

        for (Node node : map) {
            if(node.getCoordinates().getY() > maxY){
                maxY = node.getCoordinates().getY();
            }
            if(node.getCoordinates().getY() < minY){
                minY = node.getCoordinates().getY();
            }
        }
        double middle = (maxY + minY)/2;

        for(int i =0 ; i < map.size(); i++){
            Node node = map.get(i);
            double x = node.getCoordinates().getX();
            double y = node.getCoordinates().getY();
            if(y > middle){
                double diff = y - middle;
                double newY = middle - diff +1;
                node.getCoordinates().setLocation(x, newY);
            }else{
                if(y < middle){
                    double diff = middle - y;
                    double newY = middle + diff+ 1;
                    node.getCoordinates().setLocation(x, newY);
                }
            }
        }
        return map;
    }

}
