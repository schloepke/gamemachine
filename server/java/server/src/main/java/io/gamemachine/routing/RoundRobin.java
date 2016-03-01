package io.gamemachine.routing;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;

public class RoundRobin<T> {
    private final Multiset<T> counter;

    private final Iterator<T> elements;

    public RoundRobin(final List<T> elements) {
        this.counter = HashMultiset.create();
        this.elements = Iterables.cycle(elements).iterator();
    }

    public T getOne() {
        if (!elements.hasNext()) {
            return null;
        }
        final T element = this.elements.next();
        this.counter.add(element);
        return element;
    }

    public int getCount(final T element) {
        return this.counter.count(element);
    }

    public static RoundRobin<String> create(List<String> elements) {
        return new RoundRobin<>(elements);
    }
}
