package com.grandopengame.engine.core.render;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Representing camera
 */
@Getter
@Setter
public class Camera {
    protected static Camera currentCamera;
    protected Vector3f position = new Vector3f();
    protected Vector3f rotation = new Vector3f();

    public static Camera createDefault(Vector2f viewportSize) {
        if (currentCamera == null) {
            currentCamera = new Camera();
        }

        return currentCamera;
    }
}
