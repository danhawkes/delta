package co.arcs.launcher.model;

import android.content.ComponentName;

import org.immutables.value.Value;

@Value.Immutable
public abstract class App {

    public abstract String getIdentifier();

    public abstract String getLabel();

    public abstract int getIcon();

    public abstract String getComponentNameString();

    public ComponentName getComponentName() {
        return ComponentName.unflattenFromString(getComponentNameString());
    }
}
