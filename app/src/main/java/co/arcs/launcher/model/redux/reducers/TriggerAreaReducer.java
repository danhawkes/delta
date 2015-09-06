package co.arcs.launcher.model.redux.reducers;

import co.arcs.launcher.model.TriggerArea;
import co.arcs.launcher.model.ImmutableState;
import co.arcs.launcher.model.ImmutableTriggerArea;
import co.arcs.launcher.model.ImmutableTriggerAreas;
import co.arcs.launcher.model.State;
import co.arcs.launcher.model.redux.actions.Init;
import co.arcs.launcher.redux.ReduxAction;
import co.arcs.launcher.redux.ReduxReducer;

public class TriggerAreaReducer implements ReduxReducer<State> {

    @Override
    public State reduce(State state, ReduxAction action) {
        if (action instanceof Init) {
            return handle(state, (Init) action);
        } else {
            return state;
        }
    }

    private State handle(State state, Init action) {
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
    }
}
