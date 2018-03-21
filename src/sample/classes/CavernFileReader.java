package sample.classes;
import java.io.BufferedReader;
import java.awt.Point;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CavernFileReader {

    public static List<Node> readCavernFile(String fileName) throws IOException {

        // Open input.cav
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        //Read the line of comma separated text from the file
        String buffer = br.readLine();
        br.close();

        //Convert the data to an array
        String[] data = buffer.split(",");

        //Now extract data from the array - note that we need to convert from String to int as we go
        int noOfCaves = Integer.parseInt(data[0]);

        List<Node> map = new ArrayList<>();
        //Get coordinates
        //Create a new node for each point on the map
        for (int count = 1; count < ((noOfCaves*2)+1); count=count+2){
            int nodeNumber = (count +1)/2;
            String nodeName = Integer.toString(nodeNumber);
            Node node = new Node(nodeName);
            node.setCoordinates(new Point(Integer.parseInt(data[count]), Integer.parseInt(data[count+1])));
            map.add(node);
        }



        //Build connectivity matrix

        //Declare the array
        boolean[][] connected = new boolean[noOfCaves][];

        for (int row= 0; row < noOfCaves; row++){
            connected[row] = new boolean[noOfCaves];
        }
        //Now read in the data - the starting point in the array is after the coordinates
        int col = 0;
        int row = 0;

        for (int point = (noOfCaves*2)+1 ; point < data.length; point++){
            //Work through the array

            if (data[point].equals("1")){
                connected[row][col] = true;
                //if nodes are connected a neighbour is added to the adjencencyList
                map.get(row).addNeighbour(new Edge(map.get(row), map.get(col)));
            }
            else
                connected[row][col] = false;

            row++;
            if (row == noOfCaves){
                row=0;
                col++;
            }
        }

        return map;
    }
}
