package co.arcs.launcher.redux.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.arcs.launcher.redux.ReduxAction;
import co.arcs.launcher.redux.ReduxReducer;

/**
 * Reducer that combines state manipulation from multiple child reducers. This implementation
 * works on an indivisible state 'atom', so all reducers operate in the same state space. The
 * order of operations is undefined, so it is assumed that child reducers will operate of
 * separate state trees or otherwise handle an unordered call sequence.
 *
 * @param <STATE> Type of the state atom.
 */
public abstract class CombiningAtomReducer<STATE> implements ReduxReducer<STATE> {

    private final List<ReduxReducer<STATE>> reducers = new ArrayList<>();

    @SafeVarargs
    public CombiningAtomReducer(ReduxReducer<STATE>... reducers) {
        Collections.addAll(this.reducers, reducers);
    }

    @Override
    public STATE reduce(STATE state, ReduxAction action) {
        STATE newState = state;
        for (ReduxReducer<STATE> reducer : reducers) {
            newState = reducer.reduce(newState, action);
        }
        return newState;
    }
}
