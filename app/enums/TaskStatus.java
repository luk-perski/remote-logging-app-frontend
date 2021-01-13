package enums;

import java.util.Arrays;
import java.util.Optional;

public enum TaskStatus {
    NEW(0),
    IN_PROGRESS(1),
    SUSPEND(2),
    DONE(3),
    CLOSED(4),
    REOPEN(5);

    private final int order;

    TaskStatus(int order) {
        this.order = order;
    }

    public static Optional<TaskStatus> orderOf(int order) {
        return Arrays.stream(values())
                .filter(taskStatus -> taskStatus.order == order)
                .findFirst();
    }
}
