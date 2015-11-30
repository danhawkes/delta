package co.arcs.launcher.model.redux.reducers;

import co.arcs.launcher.model.ImmutableConfig;
import co.arcs.launcher.model.ImmutableState;
import co.arcs.launcher.model.State;
import co.arcs.launcher.model.redux.actions.Init;
import co.arcs.redux.Reducer;

public class ConfigReducer implements Reducer<State, Object> {

    @Override
    public State reduce(State state, Object action) {
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
