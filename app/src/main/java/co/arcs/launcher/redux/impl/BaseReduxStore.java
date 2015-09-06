package co.arcs.launcher.redux.impl;

import co.arcs.launcher.redux.ReduxAction;
import co.arcs.launcher.redux.ReduxReducer;
import co.arcs.launcher.redux.ReduxStore;
import rx.Observable;
import rx.subjects.PublishSubject;

public class BaseReduxStore<STATE> implements ReduxStore<STATE> {

    private final ReduxReducer<STATE> reducer;
    private STATE state;
    PublishSubject<Object> changesSubject = PublishSubject.create();

    public BaseReduxStore(ReduxReducer<STATE> reducer, STATE initialState) {
        this.reducer = reducer;
        this.state = initialState;
    }

    @Override
    public final STATE getState() {
        return state;
    }

    @Override
    public void setState(STATE state) {
        if (!state.equals(this.state)) {
            this.state = state;
            changesSubject.onNext(new Object());
        }
    }

    public final void dispatch(ReduxAction action) {
        setState(reducer.reduce(getState(), action));
    }

    @Override
    public final Observable<Object> changes() {
        return changesSubject;
    }
}
