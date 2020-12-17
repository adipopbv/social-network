package socialnetwork.domain;

import java.util.Objects;
import java.util.Random;

public class Id {
    private int value;

    public Id() {
        Random random = new Random();
        setValue(random.nextInt(9000) + 1000);
    }

    public Id(int value) {
        this.value = value;
    }

    public Id(String value) {
        try {
            this.value = Integer.parseInt(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("non int value");
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Id id = (Id) o;
        return value == id.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
