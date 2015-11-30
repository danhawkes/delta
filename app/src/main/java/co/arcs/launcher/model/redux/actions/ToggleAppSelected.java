package co.arcs.launcher.model.redux.actions;

import co.arcs.redux.ReduxAction;

public class ToggleAppSelected implements ReduxAction {

    public final String appIdentifier;

    public ToggleAppSelected(String appIdentifier) {
        this.appIdentifier = appIdentifier;
    }
}
