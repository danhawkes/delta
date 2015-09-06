package co.arcs.launcher.model.redux.reducers;

import co.arcs.launcher.model.ImmutableConfig;
import co.arcs.launcher.model.ImmutableState;
import co.arcs.launcher.model.State;
import co.arcs.launcher.model.redux.actions.Init;
import co.arcs.launcher.redux.ReduxAction;
import co.arcs.launcher.redux.ReduxReducer;

public class ConfigReducer implements ReduxReducer<State> {

    @Override
    public State reduce(State state, ReduxAction action) {
        if (action instanceof Init) {
            ImmutableConfig.Builder builder = ImmutableConfig.builder();
            if (state.getConfig() != null) {
                builder.from(state.getConfig());
            }
            builder.isInitialised(true);
            return ImmutableState.copyOf(state).withConfig(builder.build());
        } else {
            return state;
        }
    }
}
