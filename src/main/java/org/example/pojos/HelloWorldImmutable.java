package org.example.pojos;

import java.util.Objects;

public class HelloWorldImmutable {

    private String value;

    public HelloWorldImmutable(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HelloWorldImmutable)) return false;
        HelloWorldImmutable that = (HelloWorldImmutable) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
