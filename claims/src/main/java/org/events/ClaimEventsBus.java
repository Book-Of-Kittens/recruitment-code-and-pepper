package org.events;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ClaimEventsBus {

    private final List<Consumer<ClaimUpdatedEvent>> consumers = new ArrayList<>();

    public void subscribe(Consumer<ClaimUpdatedEvent> subscription) {
        consumers.add(subscription);
    }

    public void raiseEvent(ClaimUpdatedEvent event) {
        consumers.forEach(s -> s.accept(event));
    }

}
