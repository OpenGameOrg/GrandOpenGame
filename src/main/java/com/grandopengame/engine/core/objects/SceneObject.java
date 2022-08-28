package com.grandopengame.engine.core.objects;

import com.grandopengame.engine.core.graphics.model.Model;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.joml.Vector3f;

/**
 * Engine object, can be rendered
 */
@RequiredArgsConstructor
@Getter
public class SceneObject {
    private final Model model;
    public final Vector3f position;
    public final Vector3f rotation;
    public final Vector3f scale;
}
