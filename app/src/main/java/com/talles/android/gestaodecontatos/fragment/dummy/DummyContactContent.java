package com.talles.android.gestaodecontatos.fragment.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContactContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Long, DummyItem> ITEM_MAP = new HashMap<Long, DummyItem>();

    private static final int COUNT = 10;

    /*
    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i,"Pessoa",5));
        }
    }
    */

    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void clearList(){
        ITEMS.clear();
        ITEM_MAP.clear();
    }

    private static DummyItem createDummyItem(int position, String name, float rating) {
        return new DummyItem(position, name + " " + position, rating);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final long id;
        public final String name;
        public final float rating;

        public DummyItem(long id, String content, float rating) {
            this.id = id;
            this.name = content;
            this.rating = rating;
        }

        @Override
        public String toString() {
            return name + " " + rating;
        }
    }
}
