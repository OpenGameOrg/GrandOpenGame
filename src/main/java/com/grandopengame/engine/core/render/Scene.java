package com.grandopengame.engine.core.render;

import com.grandopengame.engine.core.graphics.model.Model;
import com.grandopengame.engine.core.objects.SceneObject;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of what should be rendered
 */
public class Scene {
    @Getter
    private List<SceneObject> objects = new ArrayList<>();

    public void addObject(SceneObject object) {
        objects.add(object);
    }

    public String getStats() {
        var vertices = objects.stream().mapToInt((object) -> object.getModel().getVertices().size()).sum();
        var faces = objects.stream().mapToInt((object) -> object.getModel().getIndices().size() / 3).sum();

        return String.format("(faces: %s, vertices: %s, objects: %s)", faces, vertices, objects.size());
    }
}
