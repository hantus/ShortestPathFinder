package sample.classes;

import java.util.List;

public class SearchState {
    private List<Node> frontiere;
    private List<Node> explored;
    private Node currentLocation;

    public SearchState(List<Node> frontiere, List<Node> explored, Node currentLocation) {
        this.frontiere = frontiere;
        this.explored = explored;
        this.currentLocation = currentLocation;
    }

    public List<Node> getFrontiere() {
        return frontiere;
    }

    public List<Node> getExplored() {
        return explored;
    }

    public Node getCurrentLocation() {
        return currentLocation;
    }

}
