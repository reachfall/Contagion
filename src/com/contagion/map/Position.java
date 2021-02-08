package com.contagion.map;

public class Position {

    private final int x;
    private final int y;


    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    @Override
    public int hashCode() {
        return x * 3803 + y * 6971;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position other = (Position)obj;
            return other.getX() == getX() && other.getY() == getY();
        }
        return false;
    }
}
