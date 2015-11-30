package co.arcs.redux.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.arcs.redux.Reducer;

/**
 * Reducer that combines state manipulation from multiple child reducers. This implementation
 * works on an indivisible state 'atom', so all reducers operate in the same state space. The
 * order of operations is undefined, so it is assumed that child reducers will operate on
 * separate state trees or otherwise handle an unordered call sequence.
 *
 * @param <STATE> Type of the state atom.
 */
public abstract class CombiningAtomReducer<STATE, ACTION> implements Reducer<STATE, ACTION> {

    private final List<Reducer<STATE, ACTION>> reducers = new ArrayList<>();

    @SafeVarargs
    public CombiningAtomReducer(Reducer<STATE, ACTION>... reducers) {
        Collections.addAll(this.reducers, reducers);
    }

    @Override
    public STATE reduce(STATE state, ACTION action) {
        STATE newState = state;
        for (Reducer<STATE, ACTION> reducer : reducers) {
            newState = reducer.reduce(newState, action);
        }
        return newState;
    }
}
