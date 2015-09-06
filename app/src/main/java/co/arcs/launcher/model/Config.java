package co.arcs.launcher.model;

import org.immutables.value.Value;

@Value.Immutable
public abstract class Config {

    public abstract boolean isInitialised();
}
