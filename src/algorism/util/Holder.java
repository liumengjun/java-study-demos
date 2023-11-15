package algorism.util;

import java.util.Objects;

/**
 * Created by liumengjun on 2023-11-15.
 */
public final class Holder<T> {

    public T value;

    public Holder() {
    }

    public Holder(T value) {
        this.value = value;
    }

    public T get() {
        return this.value;
    }

    public void set(T value) {
        this.value = value;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
