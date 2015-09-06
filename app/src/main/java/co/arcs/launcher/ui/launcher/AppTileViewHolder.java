package co.arcs.launcher.ui.launcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.raizlabs.universaladapter.ViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.arcs.launcher.R;
import rx.Observable;

public class AppTileViewHolder extends ViewHolder {

    @Bind(R.id.container) public AppTileView containerView;
    @Bind(R.id.image) public ImageView imageView;

    public AppTileViewHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.view_app_tile, parent, false));
        ButterKnife.bind(this, itemView);
    }

    public AppTileViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public Observable<Object> drops() {
        return containerView.drops();
    }
}
