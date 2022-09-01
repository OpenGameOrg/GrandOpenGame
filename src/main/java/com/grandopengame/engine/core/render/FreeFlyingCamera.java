package com.grandopengame.engine.core.render;

import com.grandopengame.engine.core.event.*;
import lombok.extern.java.Log;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Camera that can fly in any direction
 */
@Log
public class FreeFlyingCamera extends Camera {
    private static final float DEFAULT_FLY_SPEED = 0.5f;

    private Vector2f viewportSize;
    private float flySpeed;
    private boolean isChangingDirection;
    private double lastMouseX = -1;
    private double lastMouseY = -1;

    private FreeFlyingCamera() {
        super();

        EventBus.subscribeToEvent(EventType.KEY_PRESSED, (eventData) -> processKeyPressedEvent((KeyEventData) eventData));
        EventBus.subscribeToEvent(EventType.MOUSE_BUTTON_EVENT, (eventData) -> processMousePressedEvent((KeyEventData) eventData));
        EventBus.subscribeToEvent(EventType.MOUSE_MOVED, (eventData) -> processMouseMovedEvent((MousePositionEventData) eventData));
    }

    public static FreeFlyingCamera createDefault(Vector2f viewportSize) {
        var defaultCam = new FreeFlyingCamera();
        defaultCam.flySpeed = DEFAULT_FLY_SPEED;
        defaultCam.viewportSize = viewportSize;
        Camera.currentCamera = defaultCam;

        return defaultCam;
    }

    private void processKeyPressedEvent(KeyEventData eventData) {
        var key = eventData.getKeyCode();

        if (key == GLFW_KEY_RIGHT || key == GLFW_KEY_D) {
            position.x -= flySpeed;
        }
        if (key == GLFW_KEY_LEFT || key == GLFW_KEY_A) {
            position.x += flySpeed;
        }
        if (key == GLFW_KEY_UP || key == GLFW_KEY_W) {
            position.z += flySpeed;
        }
        if (key == GLFW_KEY_DOWN || key == GLFW_KEY_S) {
            position.z += flySpeed;
        }
        if (key == GLFW_KEY_SPACE) {
            position.y -= flySpeed;
        }

        if (key == GLFW_MOUSE_BUTTON_RIGHT) {
            isChangingDirection = true;
        }
    }

    private void processMouseMovedEvent(MousePositionEventData eventData) {
        if (lastMouseX == -1 || lastMouseY == -1) {
            lastMouseX = eventData.getXPos();
            lastMouseY = eventData.getYPos();

            return;
        }

        if (!isChangingDirection) return;

        var xDelta = eventData.getXPos() - lastMouseX;
        var yDelta = eventData.getYPos() - lastMouseY;

        rotation.y += yDelta / viewportSize.y;
        rotation.x += xDelta / viewportSize.x;

        log.info("rotation: " + rotation);
    }

    private void processMousePressedEvent(KeyEventData eventData) {
        if (eventData.getKeyCode() != GLFW_MOUSE_BUTTON_RIGHT) return;

        isChangingDirection = eventData.isPressed();
    }
}
