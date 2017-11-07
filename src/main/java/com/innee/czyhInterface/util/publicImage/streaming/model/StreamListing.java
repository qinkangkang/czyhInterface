package com.innee.czyhInterface.util.publicImage.streaming.model;

import com.innee.czyhInterface.util.publicImage.util.StringUtils;

/**
 * Created by bailong on 16/9/23.
 */
public final class StreamListing {
    public Item[] items;
    public String marker;

    public boolean isEOF() {
        return StringUtils.isNullOrEmpty(marker);
    }

    public String[] keys() {
        String[] keys = new String[items.length];
        int i = 0;
        for (Item item : items) {
            keys[i++] = item.key;
        }
        return keys;
    }

    private static class Item {
        public String key;
    }
}
