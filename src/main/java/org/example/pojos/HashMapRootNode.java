package org.example.pojos;

import java.util.Map;

public class HashMapRootNode {

    private final Map<Integer, HelloWorldImmutable> elements;

    public HashMapRootNode(Map<Integer, HelloWorldImmutable> elements) {this.elements = elements;}

    public Map<Integer, HelloWorldImmutable> getElements() {
        return elements;
    }
}
