package co.arcs.launcher.model;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public abstract class TriggerAreas {

    public abstract  List<TriggerArea> getItems();
}
