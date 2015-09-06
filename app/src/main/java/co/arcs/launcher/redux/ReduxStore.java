package co.arcs.launcher.redux;

import rx.Observable;

/**
 * <a href="https://rackt.github.io/redux/docs/basics/Store.html">Redux store documentation</a>
 */
public interface ReduxStore<STATE> {

    STATE getState();

    void setState(STATE state);

    void dispatch(ReduxAction action);

    Observable<Object> changes();
}