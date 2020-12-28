package enums;

import java.util.Arrays;
import java.util.Optional;

public enum Priority {
    LOW(0),
    MEDIUM(1),
    HIGH(2);

    private final int order;

    Priority(int order) {
        this.order = order;
    }

    public static Optional<Priority> orderOf(int order) {
        return Arrays.stream(values())
                .filter(priority -> priority.order == order)
                .findFirst();
    }
}
