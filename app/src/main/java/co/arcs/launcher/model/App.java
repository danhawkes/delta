package co.arcs.launcher.model;

import android.content.ComponentName;

import org.immutables.value.Value;

@Value.Immutable
public abstract class App implements Comparable<App> {

    public abstract String getIdentifier();

    public abstract String getLabel();

    public abstract int getIcon();

    public abstract String getComponentNameString();

    public ComponentName getComponentName() {
        return ComponentName.unflattenFromString(getComponentNameString());
    }

    @Override
    public int compareTo(App another) {
        return getLabel().compareToIgnoreCase(another.getLabel());
    }
}
