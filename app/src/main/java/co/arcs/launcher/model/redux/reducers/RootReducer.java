package co.arcs.launcher.model.redux.reducers;

import android.content.Context;

import co.arcs.launcher.model.State;
import co.arcs.launcher.redux.impl.CombiningAtomReducer;

public class RootReducer extends CombiningAtomReducer<State> {

    public RootReducer(Context context) {
        super(new AppReducer(context), new TriggerAreaReducer(), new ConfigReducer());
    }
}
