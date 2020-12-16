package socialnetwork.domain;

import java.util.Random;

public class Id {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Id() {
        Random random = new Random();
        setValue(random.nextInt(9000) + 1000);
    }

    public Id(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }
}
