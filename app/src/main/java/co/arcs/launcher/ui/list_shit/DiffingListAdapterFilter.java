package co.arcs.launcher.ui.list_shit;

import android.widget.Filter;

import java.util.List;

/**
 * Abstract filter base class that deals in {@link DiffFilterResults} rather than {@link
 * FilterResults}.
 */
public abstract class DiffingListAdapterFilter<T> extends Filter {

    @Override
    protected final FilterResults performFiltering(CharSequence constraint) {
        DiffFilterResults<T> diffResults = performFiltering2(constraint);

        FilterResults results = new FilterResults();
        results.count = diffResults.items.size();
        results.values = diffResults;
        return results;
    }

    protected abstract DiffFilterResults<T> performFiltering2(CharSequence constraint);

    @Override
    protected final void publishResults(CharSequence constraint, FilterResults results) {
        DiffFilterResults<T> result = (DiffFilterResults<T>) results.values;
        publishResults2(constraint, result);
    }

    protected abstract void publishResults2(CharSequence constraint, DiffFilterResults<T> results);

    public static class DiffFilterResults<T> {

        public final List<T> items;
        public final DiffUtils.ListDiff listDiff;

        public DiffFilterResults(List<T> items, DiffUtils.ListDiff listDiff) {
            this.items = items;
            this.listDiff = listDiff;
        }
    }
}
