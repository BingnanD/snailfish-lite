package me.duanbn.snailfish.util.collection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * @author bingnan.dbn
 */
@SuppressWarnings("unchecked")
public class Sets {

    public static <E> TreeSet<E> newTreeSet() {
        return new TreeSet<>();
    }

    public static <E> TreeSet<E> newTreeSet(Iterable<E> value) {
        checkNotNull(value);
        TreeSet<E> set = new TreeSet<>();
        Iterator<E> iterator = value.iterator();
        while (iterator.hasNext()) {
            set.add(iterator.next());
        }
        return set;
    }

    public static <E> TreeSet<E> newTreeSet(E... elements) {
        checkNotNull(elements);
        TreeSet<E> set = new TreeSet<>();
        Collections.addAll(set, elements);
        return set;
    }

    public static <E> HashSet<E> newHashSet() {
        return new HashSet<>();
    }

    public static <E> HashSet<E> newHashSet(Iterable<E> value) {
        checkNotNull(value);
        HashSet<E> set = new HashSet<>();
        Iterator<E> iterator = value.iterator();
        while (iterator.hasNext()) {
            set.add(iterator.next());
        }
        return set;
    }

    public static <E> HashSet<E> newHashSet(E... elements) {
        checkNotNull(elements);
        HashSet<E> set = new HashSet<>();
        Collections.addAll(set, elements);
        return set;
    }

    public static <T extends Object> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

}
