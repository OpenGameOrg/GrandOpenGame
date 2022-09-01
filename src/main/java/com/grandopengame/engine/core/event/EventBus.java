package com.grandopengame.engine.core.event;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Connect event producers and consumers
 */
@Log
public enum EventBus {
    INSTANCE;

    private final Map<EventType, List<Consumer<EventData>>> eventListeners;

    EventBus() {
        eventListeners = new HashMap<>();
    }

    public static void subscribeToEvent(EventType eventType, Consumer<EventData> consumer) {
        log.info("Subscribed to " + eventType);

        if (!INSTANCE.eventListeners.containsKey(eventType)) {
            INSTANCE.eventListeners.put(eventType, new ArrayList<>());
        }

        INSTANCE.eventListeners.get(eventType).add(consumer);
    }

    public static void broadcastEvent(EventType eventType, EventData data) {
        log.warning("Event broadcast " + eventType);

        if (!INSTANCE.eventListeners.containsKey(eventType)) return;

        INSTANCE.eventListeners.get(eventType).forEach((consumer) -> consumer.accept(data));
    }
}
