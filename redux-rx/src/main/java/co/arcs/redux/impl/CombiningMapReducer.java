package co.arcs.redux.impl;

import java.util.HashMap;
import java.util.Map;

import co.arcs.redux.Reducer;

public class CombiningMapReducer<ACTION> implements Reducer<Map<String, Object>, ACTION> {

    private final Map<String, Reducer> reducersMap = new HashMap<>();

    public CombiningMapReducer(Reducer... reducers) {
        for (Reducer reducer : reducers) {
            reducersMap.put(reducer.getClass().getName(), reducer);
        }
    }

    @Override
    public Map<String, Object> reduce(Map<String, Object> state, ACTION action) {
        HashMap<String, Object> newState = new HashMap<>(state);
        for (String key : reducersMap.keySet()) {
            Reducer reducer = reducersMap.get(key);
            Object partialState = state.get(key);
            Object newPartialState = reducer.reduce(partialState, action);
            newState.put(key, newPartialState);
        }

        return newState;
    }
}
