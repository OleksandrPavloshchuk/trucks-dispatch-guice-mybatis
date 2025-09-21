package tutorial.trucksDispatchGuiceMybatis.events.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import tutorial.trucksDispatchGuiceMybatis.domain.Assignment;

public record AssignmentCreatedOutputEvent(
        @JsonProperty("assignment") Assignment assignment
) implements OutputEvent {
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
