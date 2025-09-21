package tutorial.trucksDispatchGuiceMybatis.services;

import tutorial.trucksDispatchGuiceMybatis.events.in.InputEvent;
import tutorial.trucksDispatchGuiceMybatis.events.out.OutputEvent;

public interface InputEventService<T extends InputEvent> {
    OutputEvent apply(T src);
}
