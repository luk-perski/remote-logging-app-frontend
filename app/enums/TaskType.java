package enums;

import java.util.Arrays;
import java.util.Optional;

public enum TaskType {
    TASK(0),
    SUBTASK(1);

    private final int order;

    TaskType(int order) {
        this.order = order;
    }

    public static Optional<TaskType> orderOf(int order) {
        return Arrays.stream(values())
                .filter(taskType -> taskType.order == order)
                .findFirst();
    }
}