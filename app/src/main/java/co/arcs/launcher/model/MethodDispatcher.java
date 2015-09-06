package co.arcs.launcher.model;

import android.support.v4.util.ArrayMap;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import co.arcs.launcher.redux.ReduxAction;
import co.arcs.launcher.redux.impl.ReducerFunction;
import rx.Observable;

/**
 * Utility to do runtime dispatch of actions to {@linkplain ReducerFunction}-annotated methods.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Double_dispatch">wikipedia</a>
 */
class MethodDispatcher {

    private final Object target;
    private final Class<?> stateType;
    private final ArrayMap<Class<? extends ReduxAction>, Method> actionsToHandlers = new ArrayMap<>();
    private Method defaultHandler;
    private boolean initialised;

    public MethodDispatcher(Object target, Class<?> stateType) {
        this.target = target;
        this.stateType = stateType;
    }

    public void dispatch(ReduxAction action) {
        if (!initialised) {
            loadMethods();
            initialised = true;
        }
        try {
            Method method = actionsToHandlers.get(action.getClass());
            if (method != null) {
                method.invoke(target, action);
            } else if (defaultHandler != null) {
                defaultHandler.invoke(target, action);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error dispatching action", e);
        }
    }

    private void loadMethods() {
        for (Method method : target.getClass().getDeclaredMethods()) {

            // Ignore bridge and synthetic methods
            if (!method.isBridge() && !method.isSynthetic()) {

                boolean isUpdater = method.isAnnotationPresent(ReducerFunction.class);
                boolean isDefaultUpdater = method.isAnnotationPresent(DefaultReducer.class);

                if (isUpdater || isDefaultUpdater) {

                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length != 2) {
                        throw new RuntimeException("Annotated mathod " + method.toString() +
                                " must have two parameters (a state and an action).");
                    }

                    Class<?> type0 = parameterTypes[0];
                    if (!stateType.isAssignableFrom(type0)) {
                        throw new RuntimeException("Annotated mathod " + method.toString() +
                                " first parameter type must be assignable as " + stateType.getName() + ".");
                    }

                    Class<?> type1 = parameterTypes[1];
                    if (!ReduxAction.class.isAssignableFrom(type1)) {
                        throw new RuntimeException("Annotated mathod " + method.toString() +
                                " second parameter type must implement ReduxAction.");
                    }

                    Type returnType = method.getGenericReturnType();
                    if (!ParameterizedType.class.isInstance(returnType)) {
                        throw newInvalidReturnTypeException(method);
                    }
                    ParameterizedType returnType1 = (ParameterizedType) returnType;
                    Type rawType = returnType1.getRawType();
                    if (!(rawType instanceof Class)) {
                        throw newInvalidReturnTypeException(method);
                    }
                    if (!Observable.class.isAssignableFrom((Class<?>) rawType)) {
                        throw newInvalidReturnTypeException(method);
                    }
                    Type[] typeArguments = returnType1.getActualTypeArguments();
                    if (typeArguments.length != 1 && Void.class.isAssignableFrom(
                            typeArguments[0].getClass())) {
                        throw newInvalidReturnTypeException(method);
                    }

                    method.setAccessible(true);
                    if (isUpdater) {
                        actionsToHandlers.put((Class<? extends ReduxAction>) type1, method);
                    } else {
                        if (defaultHandler != null) {
                            throw new RuntimeException(
                                    "Two methods with DefaultHandler annotation found: " +
                                            defaultHandler.toString() + " and " +
                                            method.toString());
                        }
                        defaultHandler = method;
                    }
                }
            }
        }
    }

    private RuntimeException newInvalidReturnTypeException(Method method) {
        return new RuntimeException("Annotated method " + method.toString() +
                " must have return type of Observable<Void>.");
    }

    /**
     * Annotation for methods that update the state of the store as a result of an {@link ReduxAction}.
     * Methods with this annotation will receive actions that were not otherwise handled by an
     * {@link ReducerFunction} in the same store.
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DefaultReducer {

    }
}
