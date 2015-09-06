package co.arcs.launcher.ui.list_shit;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView.Adapter;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiffUtils {

    public static <T> ListDiff diff(List<T> before, List<T> after) {

        int beforeSize = before.size();
        int afterSize = after.size();

        Set<Integer> inserted = new HashSet<>();
        Set<Integer> removed = new HashSet<>();
        Set<Pair<Integer, Integer>> moved = new HashSet<>();

        for (int beforeIndex = 0; beforeIndex < beforeSize; beforeIndex++) {
            T item = before.get(beforeIndex);
            int afterIndex = after.indexOf(item);
            if (afterIndex == -1) {
                removed.add(beforeIndex);
            } else if (beforeIndex != afterIndex) {
                moved.add(new Pair<>(beforeIndex, afterIndex));
            }
        }

        for (int afterIndex = 0; afterIndex < afterSize; afterIndex++) {
            T item = after.get(afterIndex);
            int beforeIndex = before.indexOf(item);
            if (beforeIndex == -1) {
                inserted.add(afterIndex);
            } else if (beforeIndex != afterIndex) {
                moved.add(new Pair<>(beforeIndex, afterIndex));
            }
        }

        return new ListDiff(inserted, removed, moved);
    }

    public static class ListDiff {

        Set<Integer> inserted;
        Set<Integer> removed;
        Set<Pair<Integer, Integer>> moved;

        public ListDiff(Set<Integer> inserted,
                Set<Integer> removed,
                Set<Pair<Integer, Integer>> moved) {
            this.inserted = Collections.unmodifiableSet(inserted);
            this.removed = Collections.unmodifiableSet(removed);
            this.moved = Collections.unmodifiableSet(moved);
        }

        public void notifyAdapter(Adapter adapter) {
            for (Pair<Integer, Integer> movedIndices : moved) {
                adapter.notifyItemMoved(movedIndices.first, movedIndices.second);
            }
            for (Integer insertedIndex : inserted) {
                adapter.notifyItemInserted(insertedIndex);
            }
            for (Integer removedIndex : removed) {
                adapter.notifyItemRemoved(removedIndex);
            }
        }
    }
}
