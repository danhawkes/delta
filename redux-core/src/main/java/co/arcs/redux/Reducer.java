package co.arcs.redux;

import javax.annotation.Nullable;

/**
 * <a href="https://rackt.github.io/redux/docs/basics/Reducers.html">Redux reducer documentation</a>
 */
public interface Reducer<STATE, ACTION> {

    STATE reduce(@Nullable STATE state, ACTION action);
}
