package co.arcs.launcher.model;

import org.immutables.value.Value;

@Value.Immutable
public abstract class State {

    public abstract Apps getApps();

    public abstract TriggerAreas getTriggerAreas();

    public abstract Config getConfig();
}
