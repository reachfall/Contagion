package com.contagion.control;

import com.contagion.map.Position;
import com.contagion.tiles.DrawableType;

public enum TrackedShop {
    INSTANCE;
    private Position currentPosition;
    private Position lastPosition;
    private DrawableType currentType;
    private DrawableType lastType;
    private boolean draw;

    public Position getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Position getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Position lastPosition) {
        this.lastPosition = lastPosition;
    }

    public DrawableType getCurrentType() {
        return currentType;
    }

    public void setCurrentType(DrawableType currentType) {
        this.currentType = currentType;
    }

    public DrawableType getLastType() {
        return lastType;
    }

    public void setLastType(DrawableType lastType) {
        this.lastType = lastType;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }
}
