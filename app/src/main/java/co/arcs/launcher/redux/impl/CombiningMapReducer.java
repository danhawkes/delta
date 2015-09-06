package co.arcs.launcher.redux.impl;

import java.util.HashMap;
import java.util.Map;

import co.arcs.launcher.redux.ReduxAction;
import co.arcs.launcher.redux.ReduxReducer;

public class CombiningMapReducer implements ReduxReducer<Map<String, Object>> {

    private final Map<String, ReduxReducer> reducersMap = new HashMap<>();

    public CombiningMapReducer(ReduxReducer... reducers) {
        for (ReduxReducer reducer : reducers) {
            reducersMap.put(reducer.getClass().getName(), reducer);
        }
    }

    @Override
    public Map<String, Object> reduce(Map<String, Object> state, ReduxAction action) {
        HashMap<String, Object> newState = new HashMap<>(state);
        for (String key : reducersMap.keySet()) {
            ReduxReducer reducer = reducersMap.get(key);
            Object partialState = state.get(key);
            @SuppressWarnings("unchecked") Object newPartialState = reducer.reduce(partialState,
                    action);
            newState.put(key, newPartialState);
        }

        return newState;
    }
}
