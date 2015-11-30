package co.arcs.launcher.model.redux.reducers;

import co.arcs.launcher.model.ImmutableState;
import co.arcs.launcher.model.ImmutableTriggerArea;
import co.arcs.launcher.model.ImmutableTriggerAreas;
import co.arcs.launcher.model.State;
import co.arcs.launcher.model.TriggerArea;
import co.arcs.launcher.model.redux.actions.Init;
import co.arcs.redux.Reducer;

public class TriggerAreaReducer implements Reducer<State, Object> {

    @Override
    public State reduce(State state, Object action) {
        if (action instanceof Init) {
            return init.reduce(state, (Init) action);
        } else {
            return state;
        }
    }

    private Reducer<State, Init> init = (state, action) -> {

        ImmutableTriggerAreas.Builder builder = ImmutableTriggerAreas.builder();
        if (state != null) {
            builder.from(state.getTriggerAreas());
        }
        builder.addItems(ImmutableTriggerArea.builder()
                .edge(TriggerArea.Edge.BOTTOM)
                .width(600)
                .thickness(60)
                .midlineOffset(0)
                .build());
        return ImmutableState.copyOf(state).withTriggerAreas(builder.build());
    };
}
