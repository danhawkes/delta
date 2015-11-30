package co.arcs.redux;

/**
 * <a href="https://rackt.github.io/redux/docs/basics/Store.html">Redux store documentation</a>
 */
public interface Store<STATE> {

    STATE getState();

    void setState(STATE state);

    void dispatch(ReduxAction action);
}