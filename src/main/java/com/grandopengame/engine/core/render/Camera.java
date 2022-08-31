package com.grandopengame.engine.core.render;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

/**
 * Representing camera
 */
@Getter
@Setter
public class Camera {
    private Vector3f position;
    private Vector3f rotation;

    public static Camera getDefaultCamera() {
        var cam = new Camera();
        cam.position = new Vector3f();
        cam.rotation = new Vector3f();

        return cam;
    }
}
