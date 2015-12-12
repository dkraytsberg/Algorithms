package astar;

import java.util.Comparator;

public class AStarNode implements Comparable<AStarNode> {

    public enum NodeType {
        BLOCKED {
            public String toString(){
                return "BLOCKED";
            }
        },
        FREE {
            public String toString(){
                return "FREE";
            }
        },
        START {
            public String toString(){
                return "START";
            }
        },
        END {
            public String toString(){
                return "END";
            }
        }
    }

    public int x;
    public int y;
    public int fValue;
    public int gValue;
    public int hValue;
    public NodeType nodeType;
    public AStarNode parent;

    public AStarNode(int x, int y){
        this(x,y,0,0,0, NodeType.FREE);
    }

    public AStarNode(int x, int y, NodeType nodeType){
        this(x,y,0,0,0,nodeType);
    }

    public AStarNode(int x, int y, int fValue, int gValue, int hValue, NodeType nodeType){
        this.x = x;
        this.y = y;
        this.fValue = fValue;
        this.gValue = gValue;
        this.hValue = hValue;
        this.nodeType = nodeType;
        this.parent = null;
    }

    public boolean isBlocked(){
        return nodeType.equals(NodeType.BLOCKED);
    }

    public void setHValueAndFValue(AStarNode end){
        this.hValue = 10 * Math.abs(this.x - end.x) + Math.abs(this.y - end.y);
        this.fValue = this.hValue;
    }

    public AStarNode update(AStarNode parent, int gValue){
        this.parent = parent;
        this.gValue += gValue;
        this.fValue = this.gValue = this.hValue;
        return this;
    }

    public String toString(){
        return String.format("XY:(%d,%d) [F:%d,G:%d,H:%d] %s%n",
                x,y,fValue, gValue, hValue, nodeType);
    }

    public int compareTo(AStarNode other){
        return fValue > other.fValue ? 1 : fValue < other.fValue ? -1 : 0;
    }

}
