package org.events;

public class EventsConfig {

    public static ClaimEventsBus createEventBus() {
        return new ClaimEventsBus();
    }
}
