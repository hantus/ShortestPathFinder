package sample.classes;


// Edge represents a connection between 2 nodes
public class Edge {

    private Node startNode;
    private Node targetNode;


    public Edge( Node startNode, Node targetNode) {
        this.startNode = startNode;
        this.targetNode = targetNode;
    }


    public Node getStartNode() {
        return startNode;
    }


    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }


    public Node getTargetNode() {
        return targetNode;
    }


    public void setTargetNode(Node targetNode) {
        this.targetNode = targetNode;
    }

    public double getPathCost(){
        double pathCost;
        pathCost = this.startNode.getCoordinates().distance(this.targetNode.getCoordinates());
        return pathCost;
    }
}
