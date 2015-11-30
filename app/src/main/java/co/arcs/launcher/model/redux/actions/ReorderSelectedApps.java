package co.arcs.launcher.model.redux.actions;

import co.arcs.redux.ReduxAction;

public class ReorderSelectedApps implements ReduxAction {

    public final int a;
    public final int b;

    public ReorderSelectedApps(int a, int b) {
        this.a = a;
        this.b = b;
    }
}
