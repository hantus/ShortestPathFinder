package sample.classes;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {

    // implements Comparable to put them in the priority queue by distance
    private String name;
    private List<Edge> adjencencyList; //represents neighbours that can be accessed from this node
    private boolean visited;
    private Node predecessor;
    private Point coordinates;
    private double pathCost = Double.MAX_VALUE;
    private double estimatedGoalPath;


    public Node (String name){
        this.name = name;
        this.adjencencyList = new ArrayList<>();
    }

    public void addNeighbour(Edge edge){
        this.adjencencyList.add(edge);
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Edge> getAdjencencyList() {
        return adjencencyList;
    }

    public void setAdjencencyList(List<Edge> adjencencyList) {
        this.adjencencyList = adjencencyList;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public double getPathCost() {
        return pathCost;
    }

    public void setPathCost(double pathCost) {
        this.pathCost = pathCost;
    }


    public double getEstimatedGoalPath() {
        return estimatedGoalPath;
    }

    public void setEstimatedGoalPath(double estimatedGoalPath) {
        this.estimatedGoalPath = estimatedGoalPath;
    }

    public double getTotalDistance() {
        return this.pathCost + this.estimatedGoalPath;
    }


    public double getEstimateToGoal(Node goalNode){
        return this.getCoordinates().distance(goalNode.getCoordinates());
    }

    @Override
    public int compareTo(Node otherNode) {
        return Double.compare(this.getTotalDistance(), otherNode.getTotalDistance());
    }

}
