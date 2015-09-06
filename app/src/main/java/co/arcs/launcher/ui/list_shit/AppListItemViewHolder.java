package co.arcs.launcher.ui.list_shit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.arcs.launcher.R;

public class AppListItemViewHolder extends AbstractDraggableSwipeableItemViewHolder {

    @Bind(R.id.swipe_container) public View swipeContainer;
    @Bind(R.id.text1) public TextView t1;
    @Bind(R.id.icon) public ImageView icon;
    @Bind(R.id.drag_handle) public View dragHandle;

    public AppListItemViewHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.view_app_list_item, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public View getSwipeableContainerView() {
        return swipeContainer;
    }
}
