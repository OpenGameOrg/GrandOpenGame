package com.grandopengame.engine.core.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.joml.Vector2f;

@RequiredArgsConstructor
@Getter
public class MousePositionEventData extends EventData {
    private final double xPos;
    private final double yPos;
}
