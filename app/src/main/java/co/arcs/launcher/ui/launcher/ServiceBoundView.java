package co.arcs.launcher.ui.launcher;

import android.view.View;
import android.view.WindowManager;

public interface ServiceBoundView {

    void onCreate();

    void onAttachedToWindow();

    void onDetachedFromWindow();

    void onDestroy();

    View getView();

    WindowManager.LayoutParams getLayoutParams();
}
