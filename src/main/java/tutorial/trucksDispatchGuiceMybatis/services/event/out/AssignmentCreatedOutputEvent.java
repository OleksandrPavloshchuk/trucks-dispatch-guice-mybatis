package tutorial.trucksDispatchGuiceMybatis.services.event.out;

import tutorial.trucksDispatchGuiceMybatis.domain.Assignment;

public record AssignmentCreatedOutputEvent(Assignment assignment) implements OutputEvent {
    @Override
    public String toString() {
        return "AssignmentCreatedOutputEvent{" +
                "assignment=" + assignment +
                '}';
    }

    @Override
    public String getType() {
        return "assignmentCreated";
    }
}
