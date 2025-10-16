package me.duanbn.snailfish.util.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * List util.
 * 
 * @author bingnan.dbn
 *
 */
public class Lists {

	public static <E> ArrayList<E> newArrayList() {
		return new ArrayList<>();
	}

	public static <E> ArrayList<E> newArrayList(Iterable<E> value) {
		checkNotNull(value);
		ArrayList<E> list = new ArrayList<>();
		Iterator<E> iterator = value.iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public static <E> ArrayList<E> newArrayList(E... elements) {
		checkNotNull(elements); // for GWT
		// Avoid integer overflow when a large array is passed in
		ArrayList<E> list = new ArrayList<>();
		Collections.addAll(list, elements);
		return list;
	}

	public static <T extends Object> T checkNotNull(T reference) {
		if (reference == null) {
			throw new NullPointerException();
		}
		return reference;
	}

}
