import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Player player = new Player();
        player.start();
    }
    
    public void start() {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt(); // the total number of nodes in the level, including the gateways
        int L = in.nextInt(); // the number of links
        int E = in.nextInt(); // the number of exit gateways
        
        Node[] nodes = new Node[N];
        
        for (int i = 0; i < L; i++) {
            int N1 = in.nextInt(); // N1 and N2 defines a link between these nodes
            int N2 = in.nextInt();
            
            if (nodes[N1] == null) {
                nodes[N1] = new Node(N1);
            } 
            
            if (nodes[N2] == null) {
                nodes[N2] = new Node(N2);
            } 
        
            nodes[N1].addNeighborhood(nodes[N2]);
            nodes[N2].addNeighborhood(nodes[N1]);
        }
        
        for (int i = 0; i < E; i++) {
            int EI = in.nextInt(); // the index of a gateway node
            nodes[EI].setGateway(true);
        }

        // game loop
        while (true) {
            int SI = in.nextInt(); // The index of the node on which the Skynet agent is positioned this turn

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            // Example: 0 1 are the indices of the nodes you wish to sever the link between
            int[] find = solve(SI, nodes);
            nodes[find[0]].cut(find[1]);
            nodes[find[1]].cut(find[0]);
        
            System.out.println(find[0] + " " + find[1]);
        }
    }
    
    private int[] solve(int SI, Node[] nodes) {
        
        // Initialize Nodes
        for (Node node : nodes) {
            node.checked = false;
            node.rest_time =0;
            node.distance = 0;
        }
        
        ArrayList<Integer> check = new ArrayList<>();
        check.add(SI);
        nodes[SI].checked = true;

        // close the link near the Node at SI
        for(Node neighbor : nodes[SI].neighborhoods){
            if(neighbor.isGateway) {
                return new int[] {neighbor.id,SI};
            }
        }
        // Number of the neighborExits at Node is neighborExitNum[id] 
        int[] neighborExitNum = getNumsOfNeighborExit(nodes);
        

        // contain node's IDs that negiborExitnum[id] > 1
        ArrayList<Integer> overOneExit = new ArrayList<>();
        // contain exitNodes
        ArrayList<ArrayList<Integer>> gateways = new ArrayList<>();
 
        while(check.size() > 0) {
            int position = check.get(0);
            check.remove(0);
            
            for(Node neighbor : nodes[position].neighborhoods){
                if (!neighbor.checked) {
                    check.add(neighbor.id);
                    neighbor.checked = true;
                    neighbor.distance = nodes[position].distance + 1; // mark the distance of node using BFS
                    if(neighbor.isGateway) {
                        ArrayList<Integer> gatewayPath = new ArrayList<>();
                        gatewayPath.add(neighbor.id);
                        gatewayPath.add(position);
                        gateways.add(gatewayPath); // add (neighbor.id , position) at gateways
                        continue;
                    }
                    
                    int exitNum = neighborExitNum[neighbor.id];
                    if(exitNum<1){
                        neighbor.rest_time= nodes[position].rest_time + 1;
                    } else if(exitNum == 1){
                        neighbor.rest_time= nodes[position].rest_time;
                    } else{ // if (exitnum > 1)
                        neighbor.rest_time= nodes[position].rest_time;
                        overOneExit.add(neighbor.id); 
                    }
                    
                }
            }
        }

        
        if(overOneExit.size() > 0){ // CalCulating RestTime

            // get node's id that has minimum of resttime 

            int min = Integer.MAX_VALUE;
            int min_node = 0;
            for(int ni : overOneExit){
                Node node = nodes[ni];
    
                System.err.println("Node : " +ni + " rest_time: "+ node.rest_time);
                if(min > node.rest_time){
                    min = node.rest_time;
                    min_node = ni;
                }
            }

            // get link of previous node between gateway 
    
            int exit=0;
            for(Node neighbor: nodes[min_node].neighborhoods){
                if(neighbor.isGateway){
                    exit = neighbor.id;
                    break;
                }
            }
    
            int[] find = new int[2];
            find[0] = min_node;
            find[1] = exit;
            
            return find;
        } else{ // Calculating BFS

            //get gateway link that has minimum of distance
            int min = Integer.MAX_VALUE;
            int min_i = 0;
            for(int ei=0;ei< gateways.size();ei++){
                Node exitNode = nodes[gateways.get(ei).get(0)];
                if(min > exitNode.distance){
                    min = exitNode.distance;
                    min_i = ei;
                } 
            }

            int[] find = new int[2];
            find[0] = gateways.get(min_i).get(0);
            find[1] = gateways.get(min_i).get(1);
            return find;

        }
    }

    private int[] getNumsOfNeighborExit(Node[] nodes){
        int[] neighborExits = new int[nodes.length]; // initialize to 0
        for(int i=0;i<nodes.length;i++){
            Node node = nodes[i];
            for(int j=0;j<node.neighborhoods.size();j++){
                if(node.neighborhoods.get(j).isGateway){
                    neighborExits[i]+=1;
                }
            }
        }
        return neighborExits;
    }


    
    
    class Node {
        int id;
        int rest_time;
        int distance;
        boolean isGateway;
        boolean checked;
        ArrayList<Node> neighborhoods;
        
        public Node(int id) {
            this.id = id;
            isGateway = false;
            neighborhoods = new ArrayList<Node>();
        }
        
        public void addNeighborhood(Node neighborhood) {
            neighborhoods.add(neighborhood);
        }
        
        public void setGateway(boolean isGateway) {
            this.isGateway = isGateway;
        }

        public void cut(int id) {
            for (int i = 0; i < neighborhoods.size(); i++) {
                if (neighborhoods.get(i).id == id) {
                    neighborhoods.remove(i);
                    return;
                }
            }
        }
    }
}