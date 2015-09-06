package co.arcs.launcher.model;

import org.immutables.value.Value;

@Value.Immutable
public abstract class TriggerArea {

    public abstract Edge getEdge();

    public abstract int getMidlineOffset();

    public abstract int getThickness();

    public abstract int getWidth();

    public enum Edge {
        TOP, RIGHT, BOTTOM, LEFT
    }
}
