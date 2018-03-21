package sample.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class AStarAlgorithm {

    // implementation of A* algorithm that runs from the beginning to the end
    public static void computePath(List<Node> map){

        Node startingNode = map.get(0);
        Node goalNode = map.get(map.size() - 1);
        startingNode.setPathCost(0);
        startingNode.setEstimatedGoalPath(startingNode.getCoordinates().distance(goalNode.getCoordinates()));
        // because I implemented comparable it will add them by distance
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(startingNode);

        while(!priorityQueue.isEmpty()){
            //selecting next node for expansion, as the nodes are added according to their function cost,
            //the node with the smallest cost is selected
            Node currentNode = priorityQueue.poll();
            //setting visited variable to true so it is not added to the queue again
            currentNode.setVisited(true);
            // termination check
            if(currentNode == goalNode){
                return;
            }
            //run through all neighbours of the current node and add them to the queue if they were not visited yet
            for(Edge edge : currentNode.getAdjencencyList()){
                if(edge.getTargetNode().isVisited() == false){
                    Node neighbour = edge.getTargetNode();
                    neighbour.setEstimatedGoalPath(neighbour.getCoordinates().distance(goalNode.getCoordinates()));
                    double newPathCost = currentNode.getPathCost() + edge.getPathCost();
                    // if the new path cost is lower than the current one it is changed if a new shorter path is found
                    if(newPathCost < neighbour.getPathCost()){
                        priorityQueue.remove(neighbour);
                        neighbour.setPathCost(newPathCost);
                        neighbour.setPredecessor(currentNode);
                        priorityQueue.add(neighbour);
                    }
                }
            }
        }
    }

    // A* algorithm that returns a list of states which is then used to walk through each state manually
    public static List<SearchState> getSearchStateList(List<Node> map){

        List<SearchState> searchStates = new ArrayList<SearchState>();
        Node startingNode = map.get(0);
        List<Node> frontier = new ArrayList<>();
        frontier.add(startingNode);
        List<Node> exploredNodes = new ArrayList<>();
        SearchState state = new SearchState(frontier, null, null);
        searchStates.add(state);
        Node goalNode = map.get(map.size() - 1);
        startingNode.setPathCost(0);
        startingNode.setEstimatedGoalPath(startingNode.getCoordinates().distance(goalNode.getCoordinates()));
        // because I implemented comparable it will add them by distance
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(startingNode);

        while(!priorityQueue.isEmpty()){

            List<Node> frontiere1 = new ArrayList<>();
            List<Node> exploredStates = new ArrayList<>();

            for (Node node : exploredNodes) {
                exploredStates.add(node);
            }
            //selecting next node for expansion, as the nodes are added according to their function cost,
            //the node with the smallest cost is selected
            Node currentNode = priorityQueue.poll();
            exploredNodes.add(currentNode);

            for (Node node : priorityQueue) {
                frontiere1.add(node);
            }

            //setting visited variable to true so it is not added to the queue again
            currentNode.setVisited(true);
            //termination check
            if(currentNode == goalNode){
                SearchState state1 = new SearchState(frontiere1,exploredStates, currentNode);
                searchStates.add(state1);
                return searchStates;
            }

            //run through all neighbours of the current node and add them to the queue if they were not visited yet
            for(Edge edge : currentNode.getAdjencencyList()){
                if(edge.getTargetNode().isVisited() == false){
                    if(!frontiere1.contains(edge.getTargetNode())){
                        frontiere1.add(edge.getTargetNode());
                    }

                    Node neighbour = edge.getTargetNode();
                    neighbour.setEstimatedGoalPath(neighbour.getCoordinates().distance(goalNode.getCoordinates()));
                    double newPathCost = currentNode.getPathCost() + edge.getPathCost();
                    // if the new path cost is lower than the current one it is changed if a new shorter path is found
                    if(newPathCost < neighbour.getPathCost()){
                        priorityQueue.remove(neighbour);
                        neighbour.setPathCost(newPathCost);
                        neighbour.setPredecessor(currentNode);
                        priorityQueue.add(neighbour);
                    }
                }
            }
            SearchState state1 = new SearchState(frontiere1,exploredStates, currentNode);
            searchStates.add(state1);
        }
        return searchStates;
    }







    //finds the shortest path by starting at the goal node and following the predecessors. A* algorithm must be
    // applied first to the map
    public static List<Node> shortestPath(List<Node> map){
        List <Node> shortestPath = new ArrayList<>();
        for(Node node = map.get(map.size() -1); node != null; node = node.getPredecessor()){
            shortestPath.add(node);
        }
        Collections.reverse(shortestPath);
        return shortestPath;
    }


    // calculates the distance of a route provided
    public static double pathDistance(List<Node> map){
        double totalDistance = 0;
        System.out.println("The shortest path:");
        for(int i = 0; i < map.size()-1; i++){
            double distance = map.get(i).getCoordinates().distance(map.get(i+1).getCoordinates());
            System.out.println(map.get(i).getName() + " --> " + map.get(i+1).getName() +" = "+ distance);
            totalDistance += distance;
        }
        System.out.println("Total distance = " +totalDistance);
        return totalDistance;
    }


}
