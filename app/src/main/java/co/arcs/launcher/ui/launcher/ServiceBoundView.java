package co.arcs.launcher.ui.launcher;

import android.view.View;

public interface ServiceBoundView {

    void onCreate();

    void onAttachedToWindow();

    void onDetachedFromWindow();

    void onDestroy();

    View getView();
}
