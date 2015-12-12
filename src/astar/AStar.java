package astar;

import java.util.*;

public class AStar {

    private AStarNode[][] grid;
    private int width;
    private int height;
    private AStarNode startNode;
    private AStarNode endNode;
    private List<AStarNode> openlist = new ArrayList<>();
    private List<AStarNode> closedlist = new ArrayList<>();


    public static void main(String[] args) {
       // @formatter:off
        String[] grid = {
                "xxxxxxxx",
                "x    exx",
                "x  xxx x",
                "x  x s x",
                "xx   xxx"};
        // @formatter:on

        try{
            AStar astar = new AStar(grid,'s','e','x');
            List<AStarNode> path = astar.pathToEnd();
            path.forEach(n -> System.out.println(n));
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    public AStar(String[] grid, char start, char end, char block) throws Exception{
        AStarNode[][] nodes;
        int startNodeX = -1, startNodeY = -1, endNodeX = -1, endNodeY = -1;
        int width = grid[0].length();
        int height = grid.length;
        char type;
        AStarNode.NodeType nodeType;

        for(String s : grid)
            if(s.length() != width)
                throw new Exception("Grid width is inconsistant, should be: "
                        + width + " found : " + s.length());

        nodes = new AStarNode[height][width];

        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                type = grid[y].charAt(x);
                if(type == start) {
                    nodeType = AStarNode.NodeType.START;
                    nodes[y][x] = new AStarNode(x,y, nodeType);
                    startNodeX = x;
                    startNodeY = y;
                }
                else if(type == end) {
                    nodeType = AStarNode.NodeType.END;
                    nodes[y][x] =  new AStarNode(x,y, nodeType);
                    endNodeX = x;
                    endNodeY = y;
                }
                else {
                    nodeType = type == block ? AStarNode.NodeType.BLOCKED : AStarNode.NodeType.FREE;
                    nodes[y][x] = new AStarNode(x,y,nodeType);
                }
            }
        }

        if(startNodeX == -1) throw new Exception("Grid contains no start");
        if(endNodeX == -1) throw new Exception("Grid contains no end");



        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                nodes[y][x].setHValueAndFValue(nodes[endNodeY][endNodeX]);
            }
        }

        makeGrid(nodes, nodes[startNodeY][startNodeX], nodes[endNodeY][endNodeX]);

    }

    private void makeGrid(AStarNode[][] grid, AStarNode startNode, AStarNode endNode){

        this.width = grid[0].length;
        this.height = grid.length;
        this.grid = new AStarNode[height][width];
        this.startNode = startNode;
        this.endNode = endNode;

        for(int y = 0; y < grid.length; y++)
            for(int x = 0; x < grid[0].length; x++)
                this.grid[y][x] = grid[y][x];
    }


    public List<AStarNode> pathToEnd() throws Exception{

        List<AStarNode> path = new ArrayList<>();

        closedlist.add(startNode);
        getSurroundingNodes(startNode);

        while(!openlist.contains(endNode) && !openlist.isEmpty()){
            Collections.sort(openlist);
            AStarNode node = openlist.remove(0);
            getSurroundingNodes(node);
            closedlist.add(node);
        }

        if(endNode.parent != null){
            fillPath(path, endNode);
            return path;
        }
        else {
            throw new Exception("No path exists");
        }
    }

    private void fillPath(List<AStarNode> path, AStarNode node){
        if(node.parent != null)
            fillPath(path, node.parent);
        path.add(node);
    }

    private void getSurroundingNodes(AStarNode currentNode) {
        int x = currentNode.x;
        int y = currentNode.y;
        int gValue;

        for (int yy = -1; yy < 2; yy++) {
            for (int xx = -1; xx < 2; xx++) {
                if (yy == 0 && xx == 0) continue;

                int ycoord = y + yy;
                int xcoord = x + xx;

                if (xcoord < 0 || xcoord >= width || ycoord < 0 || ycoord >= height) continue;

                AStarNode tempNode = grid[ycoord][xcoord];

                if (closedlist.contains(tempNode) || tempNode.isBlocked()) continue;

                gValue = x == tempNode.x && y == tempNode.y ? 10 : 14;

                if ((grid[y][xcoord].isBlocked() || grid[ycoord][x].isBlocked()) && gValue == 14) continue;

                if (tempNode.parent == null || tempNode.gValue > currentNode.gValue + gValue) {
                    tempNode.update(currentNode, gValue);
                }

                if (!openlist.contains(tempNode))
                    openlist.add(tempNode);
            }
        }
    }
}
