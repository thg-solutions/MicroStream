package org.example.pojos;

import java.util.Collection;

public class CollectionRootNode<T> {
    private final Collection<T> elements;

    public CollectionRootNode(Collection<T> elements) {
        this.elements = elements;
    }

    public Collection<T> getElements() {
        return elements;
    }
}
