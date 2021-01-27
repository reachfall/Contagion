package com.contagion.pathfinding;

import com.contagion.map.Position;

public class Node implements Comparable<Node> {
    private int fScore = 0;
    private int gScore = 1000;
    private Position position;
    private Node parent;
    private String move;

    public Node(Position position){
        this.position = position;
        this.parent = null;
        this.gScore = 0;
    }

    public Node(Position position, String move){
        this.position = position;
        this.move = move;
    }

    @Override
    public String toString() {
        return "Node{" +
                "fScore=" + fScore +
                ", gScore=" + gScore +
                ", position=" + position +
                '}';
    }

    public int getfScore() {
        return fScore;
    }

    public void setfScore(int fScore) {
        this.fScore = fScore;
    }

    public int getgScore() {
        return gScore;
    }

    public void setgScore(int gScore) {
        this.gScore = gScore;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public String getMove(){
        return this.move;
    }

    @Override
    public int compareTo(Node o) {
        return this.getfScore() > o.getfScore() ? 1 : this.getfScore() < o.getfScore() ? -1 : 0;
    }

    @Override
    public int hashCode() {
        return position.getX() * 3803 + position.getY() * 6971;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node){
            Node o = (Node) obj;
            return this.getPosition().equals(o.getPosition());
        }
        return false;
    }
}
