package co.arcs.launcher;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

public interface ServiceBoundViewController {

    void onAddedToWindow(@Nullable Object info);

    void onRemovedFromWindow();

    void onDestroy();

    View getView();

    WindowManager.LayoutParams getLayoutParams();
}
