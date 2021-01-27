package com.contagion.pathfinding;

import com.contagion.map.Position;
import com.contagion.map.Map;
import com.contagion.tiles.Drawable;
import com.contagion.tiles.DrawableType;

import java.util.*;


public class Pathfinder {

    private Pathfinder() {
        throw new AssertionError();
    }

    public static ArrayList<String> reconstructionPath(Node endNode) {
        ArrayList<String> path = new ArrayList<>();
        Node node = endNode;
        while (node.getParent() != null) {
            path.add(node.getMove());
            node = node.getParent();
        }
        Collections.reverse(path);
        return path;
    }

    public static ArrayList<Node> neighbours(Position position) {
        ArrayList<Node> allNeighbours = new ArrayList<>();
        allNeighbours.add(new Node(new Position(position.getX() + 1, position.getY()), "right"));
        allNeighbours.add(new Node(new Position(position.getX() - 1, position.getY()), "left"));
        allNeighbours.add(new Node(new Position(position.getX(), position.getY() + 1), "down"));
        allNeighbours.add(new Node(new Position(position.getX(), position.getY() - 1), "up"));

        return allNeighbours;
    }

    public static boolean isNotValidNode(Node node, DrawableType type) {
        Drawable positionDrawable = Map.getInstance().getLocationToStationaryDrawable().get(node.getPosition());
        if (positionDrawable == null) {
            return true;
        }

        if (type == DrawableType.Client) {
            return positionDrawable.getObjectType() == DrawableType.Intersection || positionDrawable.getObjectType() == DrawableType.Road;
        } else {
            return positionDrawable.getObjectType() == DrawableType.SidewalkIntersection || positionDrawable.getObjectType() == DrawableType.Sidewalk;
        }

    }

    public static Integer manhattanDistance(Position start, Position end) {
        return Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY());
    }

    public static ArrayList<String> findPath(Position start, Position end, DrawableType type) {
        ArrayList<Node> open = new ArrayList<>();
        ArrayList<Node> closed = new ArrayList<>();
        open.add(new Node(start));
        Node current;

        while (!open.isEmpty()) {
            Collections.sort(open);
            current = open.get(0);
            open.remove(0);
            closed.add(current);

            if (current.getPosition().equals(end)) {
                return reconstructionPath(current);
            }

            for (Node tmp : neighbours(current.getPosition())) {
                if (isNotValidNode(tmp, type) || closed.contains(tmp)) {
                    continue;
                }

                Node neighbour = open.stream().filter(item -> item.getPosition().equals(tmp.getPosition())).findFirst().orElse(tmp);
                int tentativeGScore = current.getgScore() + 1;
                if (tentativeGScore < neighbour.getgScore() || !open.contains(neighbour)) {
                    neighbour.setParent(current);
                    neighbour.setgScore(current.getgScore());
                    neighbour.setfScore(neighbour.getgScore() + manhattanDistance(neighbour.getPosition(), end));
                    if (!open.contains(neighbour)) {
                        open.add(neighbour);
                    }
                }
            }
        }
        return null;
    }
}
