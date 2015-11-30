package co.arcs.redux.impl;

import co.arcs.redux.ReduxAction;
import co.arcs.redux.Reducer;
import co.arcs.redux.Store;
import rx.Observable;
import rx.subjects.PublishSubject;

public class RxStore<STATE> implements Store<STATE> {

    private final Reducer<STATE, Object> reducer;
    private STATE state;
    PublishSubject<Object> changesSubject = PublishSubject.create();

    public RxStore(Reducer<STATE, Object> reducer, STATE initialState) {
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

    public final Observable<Object> changes() {
        return changesSubject;
    }
}
