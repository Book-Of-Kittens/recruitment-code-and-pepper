package org.events;

public class EventsConfig {

    public static ClaimEventsBus createEventBus() {
        return new ClaimEventsBus(); /* TODO: remove if no test-specific things are added */
    }
}
