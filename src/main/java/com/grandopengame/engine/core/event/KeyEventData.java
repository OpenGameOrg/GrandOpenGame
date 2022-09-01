package com.grandopengame.engine.core.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Data provided with KEY_PRESSED event
 */
@RequiredArgsConstructor
@Getter
public class KeyEventData extends EventData {
    private final int keyCode;
    private final boolean isPressed;
}
