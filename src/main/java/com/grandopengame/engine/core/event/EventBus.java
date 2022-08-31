package com.grandopengame.engine.core.event;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Connect event producers and consumers
 */
@Log4j2
public enum EventBus {
    INSTANCE;

    private Map<Integer, List<Consumer<Object[]>>> eventListeners;

    EventBus() {
        eventListeners = new HashMap<>();
    }

    public void subscribeToEvent(int eventCode, Consumer<Object[]> consumer) {
        log.info("Subscribed to " + eventCode);

        if (!eventListeners.containsKey(eventCode)) {
            eventListeners.put(eventCode, new ArrayList<>());
        }

        eventListeners.get(eventCode).add(consumer);
    }

    public void broadcastEvent(int eventCode, Object[] data) {
        log.debug("Event broadcast " + eventCode);

        if (!eventListeners.containsKey(eventCode)) return;

        eventListeners.get(eventCode).forEach((consumer) -> consumer.accept(data));
    }
}
