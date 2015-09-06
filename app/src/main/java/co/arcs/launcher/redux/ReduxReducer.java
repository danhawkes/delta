package co.arcs.launcher.redux;

import javax.annotation.Nullable;

/**
 * <a href="https://rackt.github.io/redux/docs/basics/Reducers.html">Redux reducer documentation</a>
 */
public interface ReduxReducer<STATE> {

    STATE reduce(@Nullable STATE state, ReduxAction action);
}
