package co.arcs.launcher.redux.impl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import co.arcs.launcher.redux.ReduxAction;

/**
 * Annotation for methods that update the state of the store as a result of an {@link ReduxAction}.
 * Methods with this annotation will receieve events matching the argument type.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ReducerFunction {

}
