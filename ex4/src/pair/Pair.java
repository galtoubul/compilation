package pair;

import java.util.AbstractMap.SimpleEntry;

public class Pair<K, V> extends SimpleEntry<K, V> {
    public Pair(K key, V value) {
        super(key, value);
    }
}
