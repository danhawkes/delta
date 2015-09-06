package co.arcs.launcher.model;

import org.immutables.value.Value;

import java.util.List;
import java.util.Map;

@Value.Immutable
public abstract class Apps {

    public abstract Map<String, App> getAll();

    public abstract List<App> getSelected();
}
